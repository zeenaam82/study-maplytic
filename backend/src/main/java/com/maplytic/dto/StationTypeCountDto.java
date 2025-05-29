package com.maplytic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StationTypeCountDto {
    private String stationType;
    private long count;
}
