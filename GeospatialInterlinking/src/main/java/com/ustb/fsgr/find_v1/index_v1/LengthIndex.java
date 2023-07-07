package com.ustb.fsgr.find_v1.index_v1;


import com.ustb.fsgr.GeoSpatialRelation;

import java.util.List;
import java.util.TreeSet;

public class LengthIndex {

    BTree<GeoSpatialRelation, GeoSpatialRelation> index;

    public LengthIndex(List<GeoSpatialRelation> gsrs) {
        index = new BTree<>(getTreeSet());
        for (GeoSpatialRelation gsr : gsrs) {
            index.add(gsr);
        }
    }

    public List<GeoSpatialRelation> before(double key) {
        return index.before(sortedObject(key));
    }
    public List<GeoSpatialRelation> after(double key) {
        return index.after(sortedObject(key));
    }
    public List<GeoSpatialRelation> sub(int k1, int k2) {
        return index.subSet(sortedObject(k1), sortedObject(k2));
    }

    private TreeSet<GeoSpatialRelation> getTreeSet() {
        return new TreeSet<GeoSpatialRelation>((a, b) -> {
            if (a.getLength() == b.getLength())   {
                return (int) (b.getId() - a.getId());
            }
            // 放大
            return (int) (a.getLength() - b.getLength())*1000;
        });
    }

    private GeoSpatialRelation sortedObject(double length) {
        GeoSpatialRelation gsr = new GeoSpatialRelation();
        gsr.setId(-1L);
        gsr.setLength(length);
        return gsr;
    }

}
