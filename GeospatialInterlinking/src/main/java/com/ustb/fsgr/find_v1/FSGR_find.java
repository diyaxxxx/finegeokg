package com.ustb.fsgr.find_v1;

import com.ustb.model.GeoDataSet;
import com.ustb.fsgr.GeoSpatialRelation;
import com.ustb.fsgr.find_v1.index_v1.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author weib
 * @date 2022-11-24 15:51
 */
public class FSGR_find {
    @Data
    @AllArgsConstructor
    public static
    class Condition {
        private int edgeGeoType;
        private int edgeDetailType;
        private int v1Type;
        private int v2Type;
        private double v1Area;
        private double v2Area;
        private int angle;
        private int lowAngle;
        private int highAngle;
        private double length;
        // 查询类型 1 结构查询 2 细节查询
        private int findType;

    }

    private AngleIndex angleIndex ;
    private AreaIndex areaIndex ;
    private AzimuthIndex azimuthIndex ;
    private LengthIndex lengthIndex;
    private SRGTypeIndex srgTypeIndex;
    private ErIndex erindex;

    public FSGR_find(List<GeoSpatialRelation> gsr, GeoDataSet dataSet) {
        angleIndex = new AngleIndex(gsr);
        areaIndex = new AreaIndex(dataSet.getGeoNodes());
        azimuthIndex = new AzimuthIndex(gsr);
        lengthIndex = new LengthIndex(gsr);
        srgTypeIndex = new SRGTypeIndex(gsr);
        erindex = new ErIndex(gsr);
    }

    public List<List<GeoSpatialRelation>> find(List<Condition> cs) {
        if (cs.get(0).findType == 1) {
            return findStructual(cs);
        }
        return findDetail(cs);
    }

    public List<List<GeoSpatialRelation>> findStructual(List<Condition>  cs) {
        List<List<GeoSpatialRelation>> result = new ArrayList<>();
        List<GeoSpatialRelation> rs = srgTypeIndex.getAll(cs.get(0).getEdgeGeoType(), cs.get(0).getEdgeDetailType());
        for (GeoSpatialRelation r : rs) {
            List<GeoSpatialRelation> tmp = erindex.find(r.getFrom());
            tmp.addAll(erindex.find(r.getTo()));
            for (GeoSpatialRelation r1 : tmp) {
                if (r1 == r){
                    continue;
                }
                if (r1.getGsrDetailType() == cs.get(1).getEdgeDetailType()) {
                    List<GeoSpatialRelation> array = new ArrayList();
                    array.add(r);
                    array.add(r1);
                    result.add(array);
                }
            }
        }
        return result;
    }

    public List<List<GeoSpatialRelation>> findDetail(List<Condition>  cs) {
        List<List<GeoSpatialRelation>> tempResult = new ArrayList<>();
        int egde = 1;
        // 开始查询
        for (FSGR_find.Condition c : cs) {
            // 第二轮
            if (egde > 1) {

            }
            List<GeoSpatialRelation> rs = srgTypeIndex.getAll(c.getEdgeGeoType(), c.getEdgeDetailType());
//            if (rs.size() < top) {
//                continue;
//            }
            List<GeoSpatialRelation> rs2 = null;
            if (c.getAngle() != 30 ) {
                rs2  = angleIndex.after(c.getAngle());
            }
            List<GeoSpatialRelation> rs3 = null;
            if (c.getLowAngle() < c.getHighAngle()) {
                rs3 = azimuthIndex.find(c.getLowAngle(), c.getHighAngle());
            }
            List<GeoSpatialRelation> rs4 = lengthIndex.after(c.getLength());
            List<GeoSpatialRelation> res = union(rs, rs2, rs3, rs4);
            tempResult.add(res);
        }


        List<HashSet<Long>> results = new ArrayList<>();
        for (List<GeoSpatialRelation> temp : tempResult) {
            if (results == null) {
                for (GeoSpatialRelation r : temp) {
                    HashSet<Long> result = new HashSet<>();
                    result.add(r.getFrom().getId());
                    result.add(r.getTo().getId());
                    results.add(result);
                }
                continue;
            }
            for (GeoSpatialRelation r : temp) {
                for (HashSet<Long> result : results) {
                    if (result.contains(r.getFrom().getId()) || result.contains(r.getTo().getId())) {
                        result.add(r.getFrom().getId());
                        result.add(r.getTo().getId());
                    }
                }
            }
        }
        List<HashSet<Long>> resultsT = new ArrayList<>();
        for (HashSet<Long> result : results) {
            if (result.size() >= 3) {
                resultsT.add(result);
            }
        }
        return null;
    }


    public List<GeoSpatialRelation> union(List<GeoSpatialRelation>... rs) {
        HashSet<GeoSpatialRelation> temp = new HashSet<>();
        for (List<GeoSpatialRelation> r : rs) {
            if (r == null || r.size() == 0){
                continue;
            }
            temp.addAll(r);
        }
        HashSet<GeoSpatialRelation> result = new HashSet<>();
        for (List<GeoSpatialRelation> r : rs) {
            if (r == null || r.size() == 0){
                continue;
            }
            for (GeoSpatialRelation gr : r) {
                if (temp.contains(gr)) {
                    result.add(gr);
                }
            }
            temp = result;
            result = new HashSet<>();
        }
        return new ArrayList<>(temp);
    }

}
