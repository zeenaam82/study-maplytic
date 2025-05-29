package com.maplytic.controller;

import com.maplytic.dto.StationTypeCountDto;
import com.maplytic.service.StationService;
import com.maplytic.service.StationService.StationTypeCount;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @GetMapping("/stats/by-type")
    public List<StationTypeCount> getStationCountsByType() {
        return stationService.getStationCountsByType();
    }

    @GetMapping("/count")
    // ex: /count?lat=37.5665&lon=126.9780&radius=1000
    public long getStationCountWithinRadius(
        @RequestParam double lat,
        @RequestParam double lon,
        @RequestParam(defaultValue = "500") double radius) {

        return stationService.countStationsWithinRadius(lat, lon, radius);
    }

    @GetMapping("/count-by-type")
    // ex: /count-by-type?lat=37.5665&lon=126.9780&radius=1000
    public List<StationTypeCountDto> getStationCountsByTypeWithinRadius(
        @RequestParam double lat,
        @RequestParam double lon,
        @RequestParam(defaultValue = "500") double radius) {

        return stationService.countNearbyStationsByType(lat, lon, radius);
    }
    
}
