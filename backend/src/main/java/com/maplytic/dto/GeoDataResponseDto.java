package com.maplytic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeoDataResponseDto {
    private String busStopId;
    private String name;
    private double longitude;
    private double latitude;
    private String stationType;
}
