package com.maplytic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DistanceDto {
    private String fromId;
    private String toId;
    private double distance; // meter 단위
}
