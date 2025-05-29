package com.maplytic.controller;

import com.maplytic.dto.CenterPointDto;
import com.maplytic.dto.DensityDto;
import com.maplytic.dto.DistanceDto;
import com.maplytic.service.AnalysisService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @GetMapping("/center-point")
    // ex: /center-point?lat=37.5665&lon=126.9780&radius=1000
    public ResponseEntity<CenterPointDto> getCenterPoint(
        @RequestParam double lat,
        @RequestParam double lon,
        @RequestParam(defaultValue = "500") double radius) {

        CenterPointDto center = analysisService.getCenterPoint(lat, lon, radius);
        if (center == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(center);
    }

    @GetMapping("/density")
    // ex: /density?lat=37.5665&lon=126.9780&radius=1000
    public ResponseEntity<DensityDto> getStationDensity(
        @RequestParam double lat,
        @RequestParam double lon,
        @RequestParam(defaultValue = "500") double radius) {

        return ResponseEntity.ok(analysisService.getStationDensity(lat, lon, radius));
    }

    @GetMapping("/distance")
    // ex: /distance?fromId=1234&toId=5678
    public ResponseEntity<DistanceDto> getDistanceBetweenStops(
        @RequestParam String fromId,
        @RequestParam String toId) {

        return ResponseEntity.ok(analysisService.calculateDistance(fromId, toId));
    }
}
