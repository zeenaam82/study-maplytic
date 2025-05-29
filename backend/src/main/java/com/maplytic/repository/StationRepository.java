package com.maplytic.repository;

import com.maplytic.entity.GeoData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<GeoData, Long> {

    @Query(value = """
        SELECT station_type, COUNT(*) AS station_count
        FROM geo_data
        GROUP BY station_type
    """, nativeQuery = true)
    List<Object[]> countStationsGroupByType();
    
    @Query(value = """
        SELECT COUNT(*)
        FROM geo_data g
        WHERE ST_DWithin(
            g.location,
            ST_SetSRID(ST_MakePoint(:lon, :lat), 4326),
            :radius / 1000.0  -- km 단위
        )
    """, nativeQuery = true)
    long countNearbyStations(@Param("lat") double lat,
                            @Param("lon") double lon,
                            @Param("radius") double radiusInMeters);

    @Query(value = """
        SELECT g.station_type, COUNT(*) as count
        FROM geo_data g
        WHERE ST_DWithin(
            g.location,
            ST_SetSRID(ST_MakePoint(:lon, :lat), 4326),
            :radius / 1000.0
        )
        GROUP BY g.station_type
    """, nativeQuery = true)
    List<Object[]> countNearbyStationsByType(@Param("lat") double lat,
                                            @Param("lon") double lon,
                                            @Param("radius") double radiusInMeters);
}
