package com.ustb.fsgr.find_v1.index_v1;

import com.ustb.fsgr.GeoSpatialRelation;

import java.util.*;

/**
 * @author weib
 * @date 2022-04-20 0:09
 */
public class SixIndex implements Index{
    private HashMap<Long, Double> touchLengthMap;
    private HashMap<Long, String> nodeFromMap;
    private HashMap<Long, String> nodeToMap;
    private HashMap<Long, Integer> eAngleMap;
    private List<List<Long>> sAngleList;

    public SixIndex(int size) {
        touchLengthMap = new HashMap<>(size);
        nodeFromMap = new HashMap<>(size);
        nodeToMap = new HashMap<>(size);
        eAngleMap = new HashMap<>(size);
        sAngleList = new ArrayList<>();
        for (int i = 0; i <= 360; i++) {
            sAngleList.add(new ArrayList<>());
        }
    }

    public void add(List<GeoSpatialRelation> gsrs) {
        for (GeoSpatialRelation gsr : gsrs) {
            touchLengthMap.put(gsr.getId(), gsr.getLength());
            nodeFromMap.put(gsr.getId(), gsr.getFrom().getGeoInfo().getName());
            nodeToMap.put(gsr.getId(), gsr.getTo().getGeoInfo().getName());
            eAngleMap.put(gsr.getId(), gsr.getAzimuth().getHigh());
            sAngleList.get(gsr.getAzimuth().getLow()+180).add(gsr.getId());
        }
    }


    @Override
    public List<String> find(int sAngle, int eAngle) {
        Set<Long> set = new HashSet<>();
        for (int i = sAngle; i <= eAngle; i++) {
            set.addAll(sAngleList.get(i+180));
        }
        List<String> result = new ArrayList<>();
        for (Long id : set) {
            if (eAngleMap.get(id) <= eAngle) {
                result.add (nodeFromMap.get(id) + " " + nodeToMap.get(id));
            }
        }
        return result;
    }

    @Override
    public List<GeoSpatialRelation> find(int sAngle, int eAngle, int length, int n, boolean head) {
        Set<Long> set = new HashSet<>();
        for (int i = sAngle; i <= eAngle; i++) {
            set.addAll(sAngleList.get(i+180));
        }
        Set<Long> set2 = new HashSet();
        for (Long id : set) {
            if (eAngleMap.get(id) <= eAngle) {
                set2.add(id);
            }
        }
        Set<Long> result = new HashSet<>();
        if (head) {
            for (Long id : set2) {
                if (touchLengthMap.get(id) * n <= length) {
                    result.add(id);
                }
            }
        } else {
            for (Long id : set2) {
                if (touchLengthMap.get(id) * n > length) {
                    result.add(id);
                }
            }
        }
        return null;
    }
}
