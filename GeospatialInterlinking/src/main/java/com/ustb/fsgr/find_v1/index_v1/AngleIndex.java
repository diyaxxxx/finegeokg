package com.ustb.fsgr.find_v1.index_v1;

import com.ustb.fsgr.GeoSpatialRelation;

import java.util.List;
import java.util.TreeSet;

public class AngleIndex {
    BTree<GeoSpatialRelation, GeoSpatialRelation> index;

    public AngleIndex(List<GeoSpatialRelation> gsrs) {
        index = new BTree<>(getTreeSet());
        for (GeoSpatialRelation gsr : gsrs) {
            index.add(gsr);
        }
    }

    public List<GeoSpatialRelation> before(int key) {
        return index.before(sortedObject(key));
    }
    public List<GeoSpatialRelation> after(int key) {
        return index.after(sortedObject(key));
    }
    public List<GeoSpatialRelation> sub(int k1, int k2) {
        return index.subSet(sortedObject(k1), sortedObject(k2));
    }


    private TreeSet<GeoSpatialRelation> getTreeSet() {
        return new TreeSet<GeoSpatialRelation>((a, b) -> {
            if (a.getAngle() == b.getAngle())   {
                return (int) (b.getId() - a.getId());
            }
            // 放大
            return (int) (a.getAngle() - b.getAngle());
        });
    }

    private GeoSpatialRelation sortedObject(int angle) {
        GeoSpatialRelation gsr = new GeoSpatialRelation();
        gsr.setId(-1L);
        gsr.setLength(angle);
        return gsr;
    }
}
