package com.maplytic;

import com.maplytic.service.GeoDataService;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final GeoDataService geoDataService;

    @Override
    public void run(String... args) throws Exception {
        try {
            geoDataService.saveGeoDataFromClasspathResource("data/bus_stops.csv");
            System.out.println("CSV 데이터 DB 저장 완료");
        // } catch (IOException | CsvValidationException e) {
        } catch (Exception e) {
            System.err.println("CSV 저장 중 에러 발생");
            e.printStackTrace();
        }
    }
    
}
