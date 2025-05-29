package com.maplytic.service;

import com.maplytic.dto.CenterPointDto;
import com.maplytic.dto.DensityDto;
import com.maplytic.dto.DistanceDto;
import com.maplytic.repository.AnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final AnalysisRepository analysisRepository;

    @Transactional(readOnly = true)
    public CenterPointDto getCenterPoint(double lat, double lon, double radius) {
        List<Object[]> result = analysisRepository.findCenterPointWithinRadius(lat, lon, radius);
        if (result.isEmpty()) return null;

        Object[] row = result.get(0);
        if (row.length < 2 || row[0] == null || row[1] == null) return null;

        double avgLat = ((Number) row[0]).doubleValue();
        double avgLon = ((Number) row[1]).doubleValue();
        return new CenterPointDto(avgLat, avgLon);
    }

    @Transactional(readOnly = true)
    public DensityDto getStationDensity(double lat, double lon, double radius) {
        int count = analysisRepository.countStationsWithinRadius(lat, lon, radius);
        return new DensityDto(count, radius);
    }

    @Transactional(readOnly = true)
    public DistanceDto calculateDistance(String fromId, String toId) {
        Double distance = analysisRepository.calculateDistanceBetweenStops(fromId, toId);
        return new DistanceDto(fromId, toId, distance != null ? distance : 0.0);
    }
}
