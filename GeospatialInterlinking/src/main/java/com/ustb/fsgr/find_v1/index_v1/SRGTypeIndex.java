package com.ustb.fsgr.find_v1.index_v1;

import com.ustb.fsgr.GeoSpatialRelation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author weib
 * @date 2022-07-06 14:23
 * 边类型索引
 */
public class SRGTypeIndex {

    HashMap<Integer, HashMap<Integer, HashSet<GeoSpatialRelation>>> index = new HashMap<>();


    public SRGTypeIndex(List<GeoSpatialRelation> gsrs) {
        for (GeoSpatialRelation gsr : gsrs) {
            int gt = gsr.getGsrGeoType();
            int dt = gsr.getGsrDetailType();
            if (index.get(gt) == null) {
                HashMap<Integer, HashSet<GeoSpatialRelation>> map = new HashMap<>();
                index.put(gt, map);
            }
            if (index.get(gt).get(dt) == null) {
                HashSet<GeoSpatialRelation> set = new HashSet<>();
                index.get(gt).put(dt, set);
            }
            index.get(gt).get(dt).add(gsr);
        }
    }

    public List<GeoSpatialRelation> getAll(int geoType, int detailType) {
        return new ArrayList<>(index.get(geoType).get(detailType));
    }
    public List<GeoSpatialRelation> getAll(int geoType) {
        List<GeoSpatialRelation> result = new ArrayList<>();
        for (HashSet<GeoSpatialRelation> sgrs : index.get(geoType).values()) {
            result.addAll(sgrs);
        }
        return result;
    }


}
