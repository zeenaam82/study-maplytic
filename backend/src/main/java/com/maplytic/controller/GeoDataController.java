package com.maplytic.controller;

import com.maplytic.dto.GeoDataResponseDto;
import com.maplytic.entity.GeoData;
import com.maplytic.service.GeoDataService;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/geodata")
@RequiredArgsConstructor
public class GeoDataController {
    private final GeoDataService geoDataService;

    @GetMapping
    public List<GeoDataResponseDto> getAllStations() {
        List<GeoData> geoDataList = geoDataService.findAllGeoData();

        return geoDataList.stream().map(data -> {
            Point loc = data.getLocation();
            return new GeoDataResponseDto(
                data.getBusStopId(),
                data.getName(),
                loc.getX(),  // longitude(경도)
                loc.getY(),  // latitude(위도)
                data.getStationType()
            );
        }).collect(Collectors.toList());
    }

    @GetMapping("/nearby")
    // ex: /nearby?lat=37.5665&lon=126.9780&radius=500
    public List<GeoDataResponseDto> getNearbyStations(
        @RequestParam double lat,
        @RequestParam double lon,
        @RequestParam(defaultValue = "500") double radius) {

         // 위도 범위 체크
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("위도는 -90에서 90 사이여야 합니다.");
        }
        // 경도 범위 체크
        if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException("경도는 -180에서 180 사이여야 합니다.");
        }
        // 최대 반경 제한 (예: 5000m)
        if (radius <= 0 || radius > 5000) {
            throw new IllegalArgumentException("반경은 1~5000 미터 사이여야 합니다.");
        }

        List<GeoData> nearby = geoDataService.findNearbyStations(lat, lon, radius);

        return nearby.stream().map(data -> {
            Point loc = data.getLocation();
            return new GeoDataResponseDto(
                data.getBusStopId(),
                data.getName(),
                loc.getX(),  // longitude
                loc.getY(),  // latitude
                data.getStationType()
            );
        }).collect(Collectors.toList());
    }

    @GetMapping("/search")
    // ex: /search?keyword=강남
    public List<GeoDataResponseDto> searchStations(@RequestParam String keyword) {
        return geoDataService.searchStationsByName(keyword)
            .stream()
            .map(data -> {
                Point loc = data.getLocation();
                return new GeoDataResponseDto(
                    data.getBusStopId(),
                    data.getName(),
                    loc.getX(),
                    loc.getY(),
                    data.getStationType()
                );
            })
            .collect(Collectors.toList());
    }

    @GetMapping("/filter")
    // ex: /filter?type=가상
    public List<GeoDataResponseDto> filterStations(@RequestParam String type) {
        return geoDataService.filterStationsByType(type)
            .stream()
            .map(data -> {
                Point loc = data.getLocation();
                return new GeoDataResponseDto(
                    data.getBusStopId(),
                    data.getName(),
                    loc.getX(),
                    loc.getY(),
                    data.getStationType()
                );
            })
            .collect(Collectors.toList());
    }

    @GetMapping("/nearest")
    // ex: /nearest?lat=37.5665&lon=126.9780
    public GeoDataResponseDto getNearestStation(
            @RequestParam double lat,
            @RequestParam double lon) {

        GeoData nearest = geoDataService.findNearestStation(lat, lon);
        Point loc = nearest.getLocation();

        return new GeoDataResponseDto(
            nearest.getBusStopId(),
            nearest.getName(),
            loc.getX(), // longitude
            loc.getY(), // latitude
            nearest.getStationType()
        );
    }
    
}
