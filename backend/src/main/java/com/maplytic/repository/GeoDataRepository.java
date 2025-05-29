package com.maplytic.repository;

import com.maplytic.entity.GeoData;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoDataRepository extends JpaRepository<GeoData, Long> {
    @Query(value = """
        SELECT g.*
        FROM geo_data g
        WHERE ST_DWithin(
            g.location,
            ST_SetSRID(ST_MakePoint(:lon, :lat), 4326),
            :radius  -- radius는 미터 단위로 전달하세요
        )
    """, nativeQuery = true)
    List<GeoData> findNearby(@Param("lat") double lat,
                         @Param("lon") double lon,
                         @Param("radius") double radiusInMeters);

    @Query("""
        SELECT g FROM GeoData g
        WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<GeoData> searchByName(@Param("keyword") String keyword);

    @Query(value = """
        SELECT g.*
        FROM geo_data g
        WHERE LOWER(g.station_type) LIKE LOWER(CONCAT('%', :type, '%'))
    """, nativeQuery = true)
    List<GeoData> findByStationType(@Param("type") String type);

    @Query(value = """
        SELECT g.*
        FROM geo_data g
        ORDER BY g.location <-> ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)
        LIMIT 1
    """, nativeQuery = true)
    GeoData findNearest(@Param("lat") double lat, @Param("lon") double lon);

}
