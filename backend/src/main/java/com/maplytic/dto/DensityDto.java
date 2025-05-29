package com.maplytic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DensityDto {
    private int count;
    private double radius; // meter 단위
}
