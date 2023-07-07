package com.ustb.fsgr.find_v2.index;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.ustb.fsgr.GeoSpatialRelation;
import com.ustb.model.SgrType;

import java.util.*;

/**
 * @author weib
 * @date 2023-02-23 11:56
 * 查询索引
 * bloom + r*
 */
public class SgrIndex {

    private Map<SgrType, SubIndex> index = new HashMap<>();

    public void build(List<GeoSpatialRelation> gsrs) {
        // bloom 大小
        int num = gsrs.size() / 6;
        for (GeoSpatialRelation gsr : gsrs) {
            SgrType type = SgrType.getType(gsr.getGsrGeoType());
            SubIndex subIndex = index.putIfAbsent(type, new SubIndex(num, type));
            if (subIndex == null) {
                subIndex = index.get(type);
            }
            // 设置bloom
            long bloomId = Long.parseLong(gsr.getFrom().getId() + "" + gsr.getTo().getId());
            subIndex.putBloom(bloomId);
            if (type == SgrType.LS_LS) {
                subIndex.put(gsr.getAngle(), gsr);
            } else {
                subIndex.put(gsr.getAngle(), gsr.getLength(), gsr);
            }
        }
    }

    public List<GeoSpatialRelation> find(int sgrTypeCode, int aS, int aE, double ls, double le){
        SgrType type = SgrType.getType(sgrTypeCode);
        SubIndex subIndex = index.get(type);
        if (type == SgrType.LS_LS) {
            List<GeoSpatialRelation> rs = new ArrayList<>();
            for (Set<GeoSpatialRelation> r : new ArrayList<Set>(subIndex.rbTree.subMap(aS, aE).values())) {
                rs.addAll(r);
            }
            return rs;
        }
        List<TreeMap> angleMapList = new ArrayList<> ((subIndex.rbTree.subMap(ls, le).values()));
        List<GeoSpatialRelation> rs = new ArrayList<>();
        for (TreeMap map : angleMapList) {
            for (Set<GeoSpatialRelation> r : new ArrayList<Set>(map.subMap(aS, aE).values())) {
                rs.addAll(r);
            }

        }
        return rs;
    }

    public boolean maightExsit(Long fromId, Long toId, int sgrTypeCode) {
        SgrType type = SgrType.getType(sgrTypeCode);
        SubIndex subIndex = index.get(type);
        long bloomId = Long.parseLong(fromId + "" + toId);
        if (subIndex.bf.mightContain(bloomId)){
            return true;
        }
        bloomId = Long.parseLong(toId + "" + fromId);
        return subIndex.bf.mightContain(bloomId);
    }


    private static class SubIndex {
        private BloomFilter bf;
        // 红黑树
        private TreeMap rbTree;

        public SubIndex(int num , SgrType type) {

            bf = BloomFilter.create(Funnels.longFunnel(), num);
            if (type == SgrType.LS_LS) {
                rbTree = new TreeMap<Integer, Set<GeoSpatialRelation>>();
            } else {
                rbTree = new TreeMap<Double, TreeMap<Integer, Set<GeoSpatialRelation>>>();
            }
        }

        public void putBloom(long key) {
            bf.put(key);
        }




        protected void put(int angle, GeoSpatialRelation r) {
            Set set = null;
            if ((set = (Set) rbTree.get(angle)) == null) {
                set = new HashSet<>();
                rbTree.put(angle, set);
            }
            set.add(r);
        }
        protected void put(int angle, double length, GeoSpatialRelation r) {
            Map subMap = null;
            Set set = null;
            if ((subMap = (Map) rbTree.get(length)) == null) {
                subMap = new TreeMap<>();
                rbTree.put(length, subMap);
            }
            if ((set = (Set) subMap.get(angle)) == null) {
                set = new HashSet();
                subMap.put(angle, set);
            }
            set.add(r);
        }

    }
}
