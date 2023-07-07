package com.ustb.fsgr.find_v1.index_v1;

import com.ustb.fsgr.GeoSpatialRelation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weib
 * @date 2022-04-20 0:00
 */
public class NormalIndex implements Index{

    private List<GeoSpatialRelation> index;

    public NormalIndex() {
        index = new ArrayList<>();
    }

    public NormalIndex(List<GeoSpatialRelation> index) {
        this.index = index;
    }


    public int getSize() {
        return index.size();
    }
    public void add(List<GeoSpatialRelation> data) {
        index.addAll(data);
    }
    @Override
    public List<GeoSpatialRelation> find(int sAngle, int eAngle) {
        List<GeoSpatialRelation> result = new ArrayList<>();
        for (GeoSpatialRelation gsr : index) {
            if (gsr.getAzimuth().getLow() >= sAngle && gsr.getAzimuth().getHigh() <= eAngle) {
                result.add(gsr);
            }
        }
        return result;
    }




}
