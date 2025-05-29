package com.maplytic.config;

import com.maplytic.entity.GeoData;
import com.maplytic.repository.GeoDataRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class GeoDataCsvInitializer implements CommandLineRunner {

    private final GeoDataRepository geoDataRepository;

    @Override
    public void run(String... args) throws Exception {
        if (geoDataRepository.count() > 0) {
            return; // 이미 데이터가 있으면 스킵
        }

        ClassPathResource resource = new ClassPathResource("data/bus_stations.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        String line;
        reader.readLine(); // 헤더 스킵

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length < 5) continue;

            String busStopId = parts[0];
            String name = parts[1];
            double lon = Double.parseDouble(parts[2]);
            double lat = Double.parseDouble(parts[3]);
            String type = parts[4];

            Point point = geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(lon, lat));

            GeoData geoData = new GeoData();
            geoData.setBusStopId(busStopId);
            geoData.setName(name);
            geoData.setLocation(point);
            geoData.setStationType(type);

            geoDataRepository.save(geoData);
        }

        System.out.println("CSV 데이터 DB 저장 완료");
    }
}
