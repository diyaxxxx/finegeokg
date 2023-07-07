package com.ustb.fsgr.find_v1.index_v1;

import com.ustb.model.Azimuth;
import com.ustb.fsgr.GeoSpatialRelation;

import java.util.*;

/**
 * @author weib
 * @date 2022-04-13 0:50
 * 方位索引
 */
public class AzimuthIndex implements Index{

    private List<TreeSet<GeoSpatialRelation>> index;

    public AzimuthIndex() {

        index = new ArrayList<>(361);
        for (int i = 0; i < 361; i++) {
            index.add(getTreeSet());
        }
    }

    public AzimuthIndex(List<GeoSpatialRelation> nodes) {
        this();
        for (GeoSpatialRelation node : nodes) {
            add(node);
        }
    }

    public void add(GeoSpatialRelation gsr) {
        if (gsr.getAzimuth() == null) {
            return;
        }
        int i = gsr.getAzimuth().getLow() + 180;
        index.get(i).add(gsr);
    }

    public void addAll(List<GeoSpatialRelation> gsrs) {
        for (GeoSpatialRelation gsr : gsrs) {
            add(gsr);
        }
    }

    /**
     * 查找 从 s - e 的角度的所有关系
     * @param sAngle
     * @param eAngle
     * @return
     */
    @Override
    public List<GeoSpatialRelation> find(int sAngle, int eAngle) {
        List<GeoSpatialRelation> result = new ArrayList<>();
        for (int i = sAngle; i < eAngle; i++) {
            // 30 - 60
            // .headSet(sortedObject(eAngle))
            result.addAll(index.get(i+180).headSet(sortedObject(eAngle)));
        }
        return result;
    }



    private TreeSet<GeoSpatialRelation> getTreeSet() {
        return new TreeSet<GeoSpatialRelation>((a, b) -> {
            if (a.getAzimuth().getHigh() == b.getAzimuth().getHigh())   {
                return (int) (b.getId() - a.getId());
            }
            return a.getAzimuth().getHigh() - b.getAzimuth().getHigh();
        });
    }


    private GeoSpatialRelation sortedObject(int high) {
        GeoSpatialRelation gsr = new GeoSpatialRelation();
        gsr.setId(-1L);
        Azimuth a = new Azimuth();
        a.setHigh(high);
        gsr.setAzimuth(a);
        return gsr;
    }



}
