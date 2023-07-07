package com.ustb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

import java.io.Serializable;

/**
 * @author weib
 * @date 2022-04-03 20:29
 * 空间对象节点
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GeoNode implements Serializable {
    private Long id;
    private Geometry geometry;
    private GeoInfo geoInfo;
    private Coordinate center;


    public GeoNode(Long id, Geometry geometry, GeoInfo geoInfo) {
        this.id = id;
        this.geometry = geometry;
        this.geoInfo = geoInfo;
    }

    public Coordinate getCenter(){
        if (center != null) {
            return center;
        }
        Coordinate[] coordinates = geometry.getCoordinates();
        double x = 0;
        double y = 0;
        for (Coordinate c : coordinates) {
            x += c.x;
            y += c.y;
        }
        center = new Coordinate(x/coordinates.length, y/coordinates.length);
        return center;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeoInfo implements Serializable{
        private String name;
        // 描述
        private String des;
        // 地类类型 123 点线面
        private int geoType;
        // 具体类型 1-8  超市 办公楼 干渠 农林耕艹
        private int detailType;

        private double length;
        private double area;
    }
}
