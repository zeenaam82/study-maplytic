package com.maplytic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "geo_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bus_stop_id", unique = true, nullable = false)
    private String busStopId;

    @Column(name = "ars_id")
    private String arsId;

    @Column(nullable = false)
    private String name;

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point location;

    @Column(name = "station_type")
    private String stationType;

    public GeoData(String busStopId, String arsId, String name, Point location, String stationType) {
        this.busStopId = busStopId;
        this.arsId = arsId;
        this.name = name;
        this.location = location;
        this.stationType = stationType;
    }
    
}
