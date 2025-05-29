package com.maplytic.service;

import com.maplytic.entity.GeoData;
import com.maplytic.repository.GeoDataRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeoDataService {

    private final GeoDataRepository geoDataRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    private final EntityManager entityManager;

    @Transactional
    public void saveGeoDataFromCsv(InputStream inputStream) throws IOException, CsvValidationException {
        Set<String> existingIds = geoDataRepository.findAll().stream()
            .map(GeoData::getBusStopId)
            .collect(Collectors.toSet());
            
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String[] line;
            reader.readNext(); // 헤더 스킵
            int count = 0;

            while ((line = reader.readNext()) != null) {
                if (line.length < 6) {
                    System.err.println("잘못된 행: " + Arrays.toString(line));
                    continue;
                }

                try {
                    String busStopId = line[0];
                    if (existingIds.contains(busStopId)) continue;

                    String arsId = line[1];
                    String name = line[2];
                    double longitude = Double.parseDouble(line[3]);
                    double latitude = Double.parseDouble(line[4]);
                    String stationType = line[5];

                    Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));

                    GeoData geoData = GeoData.builder()
                            .busStopId(busStopId)
                            .arsId(arsId)
                            .name(name)
                            .location(location)
                            .stationType(stationType)
                            .build();

                    geoDataRepository.save(geoData);
                    existingIds.add(busStopId);

                    if (++count % 100 == 0) {
                        entityManager.flush();
                        entityManager.clear();
                    }
                } catch (Exception e) {
                    System.err.println("행 처리 중 오류 발생: " + Arrays.toString(line));
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Transactional
    public void saveGeoDataFromClasspathResource(String resourcePath) throws IOException, CsvValidationException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        try (InputStream inputStream = resource.getInputStream()) {
            saveGeoDataFromCsv(inputStream);
        }
    }

    @Transactional(readOnly = true)
    public List<GeoData> findAllGeoData() {
        return geoDataRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<GeoData> findNearbyStations(double lat, double lon, double radius) {
    return geoDataRepository.findNearby(lat, lon, radius);
    }

    @Transactional(readOnly = true)
    public List<GeoData> searchStationsByName(String keyword) {
        return geoDataRepository.searchByName(keyword);
    }

    @Transactional(readOnly = true)
    public List<GeoData> filterStationsByType(String stationType) {
        return geoDataRepository.findByStationType(stationType);
    }

    @Transactional(readOnly = true)
    public GeoData findNearestStation(double lat, double lon) {
        return geoDataRepository.findNearest(lat, lon);
    }

}
