package com.ustb.fsgr.find_v1.index_v1;

import com.ustb.fsgr.GeoSpatialRelation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weib
 * @date 2022-05-18 14:52
 */
public interface Index {

    List find(int sAngle, int eAngle);


    /**
     *
     * @param sAngle
     * @param eAngle
     * @return
     *
     *  获取大于 length 的 或者小于length的
     *  n 为原始长度倍数
     */
    default List<GeoSpatialRelation> find(int sAngle, int eAngle, int length, int n, boolean head) {
        List<GeoSpatialRelation> allRsr = find(sAngle, eAngle);
        List<GeoSpatialRelation> result = new ArrayList<>();
        if (head) {
            for (GeoSpatialRelation g : allRsr) {
                if (g.getLength()*n <= length)
                {
                    result.add(g);
                }
            }
        } else {
            for (GeoSpatialRelation g : allRsr) {
                if (g.getLength()*n > length)
                {
                    result.add(g);
                }
            }
        }

        return result;
    }

}
