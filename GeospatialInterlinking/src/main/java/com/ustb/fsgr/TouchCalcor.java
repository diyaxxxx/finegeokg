package com.ustb.fsgr;

import org.locationtech.jts.geom.Geometry;

/**
 * @author weib
 * @date 2022-04-12 20:11
 * 相接长度计算
 */
public class TouchCalcor {

    private static final double UNIT_LENGTH = 1D;

    public static double calc(Geometry g1, Geometry g2) {
        return calc(g1, g2, UNIT_LENGTH);
    }

    /**
     * 单位长度
     * @param g1
     * @param g2
     * @param UNIT_LENGTH
     * @return
     */
    public static double calc(Geometry g1, Geometry g2, double UNIT_LENGTH) {
        try {
            Geometry u = g1.intersection(g2);
            return u.getLength() * UNIT_LENGTH;
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
//    public static double calc2(Geometry g1, Geometry g2) {
//        Geometry u = g1.union(g2);
//        return (g1.getLength() + g2.getLength() - u.getLength()) / 2;
//    }
}
