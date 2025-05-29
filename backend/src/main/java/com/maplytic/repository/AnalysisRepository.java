package com.maplytic.repository;

import com.maplytic.entity.GeoData;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisRepository extends JpaRepository<GeoData, Long> {

    @Query(value = """
    SELECT AVG(ST_Y(location)) as avg_lat, AVG(ST_X(location)) as avg_lon
        FROM geo_data
        WHERE ST_DWithin(
            location,
            ST_SetSRID(ST_MakePoint(:lon, :lat), 4326),
            :radius / 1000.0
        )
    """, nativeQuery = true)
    List<Object[]> findCenterPointWithinRadius(@Param("lat") double lat,
                                        @Param("lon") double lon,
                                        @Param("radius") double radiusInMeters);

    @Query(value = """
        SELECT COUNT(*)
        FROM geo_data
        WHERE ST_DWithin(
            location,
            ST_SetSRID(ST_MakePoint(:lon, :lat), 4326),
            :radius / 1000.0
        )
    """, nativeQuery = true)
    int countStationsWithinRadius(@Param("lat") double lat,
                                @Param("lon") double lon,
                                @Param("radius") double radiusInMeters);

    @Query(value = """
        SELECT ST_DistanceSphere(a.location, b.location)
        FROM geo_data a, geo_data b
        WHERE a.bus_stop_id = :fromId AND b.bus_stop_id = :toId
    """, nativeQuery = true)
    Double calculateDistanceBetweenStops(@Param("fromId") String fromId,
                                        @Param("toId") String toId);

}
