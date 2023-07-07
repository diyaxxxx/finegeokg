package com.ustb.fsgr.find_v1.index_v1;

import com.ustb.model.GeoNode;
import com.ustb.fsgr.GeoSpatialRelation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author weib
 * @date 2022-11-24 16:38
    点-边索引
 */

public class ErIndex {
    private HashMap<GeoNode, HashSet<GeoSpatialRelation>> index = new HashMap<>();

    public ErIndex(List<GeoSpatialRelation> sgrs) {
        for (GeoSpatialRelation sgr : sgrs) {
            if (!index.containsKey(sgr.getFrom())) {
                index.put(sgr.getFrom(), new HashSet<>());
            }
            if (!index.containsKey(sgr.getTo())) {
                index.put(sgr.getTo(), new HashSet<>());
            }
            index.get(sgr.getFrom()).add(sgr);
            index.get(sgr.getTo()).add(sgr);
        }
    }


    public List<GeoSpatialRelation> find(GeoNode node) {
        return new ArrayList<>(index.get(node));
    }
}
