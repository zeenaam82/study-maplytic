package com.maplytic.service;

import com.maplytic.dto.StationTypeCountDto;
import com.maplytic.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;

    public static record StationTypeCount(String stationType, long count) {}
    @Transactional(readOnly = true)
    public List<StationTypeCount> getStationCountsByType() {
        List<Object[]> results = stationRepository.countStationsGroupByType();

        return results.stream()
            .map(row -> new StationTypeCount(
                (String) row[0],
                ((Number) row[1]).longValue()
            ))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countStationsWithinRadius(double lat, double lon, double radius) {
        return stationRepository.countNearbyStations(lat, lon, radius);
    }

    @Transactional(readOnly = true)
    public List<StationTypeCountDto> countNearbyStationsByType(double lat, double lon, double radius) {
        return stationRepository.countNearbyStationsByType(lat, lon, radius)
            .stream()
            .map(obj -> new StationTypeCountDto((String) obj[0], ((Number) obj[1]).longValue()))
            .collect(Collectors.toList());
    }
}
