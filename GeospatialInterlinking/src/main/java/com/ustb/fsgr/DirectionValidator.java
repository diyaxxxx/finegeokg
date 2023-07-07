package com.ustb.fsgr;

import com.ustb.model.Azimuth;
import com.ustb.model.GeoNode;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;

import java.util.Arrays;

/**
 * @author weib
 * @date 2022-04-03 21:54
 * 方位验证
 */
public class DirectionValidator {

    // todo 跟topo关系联合起来 继承一个父类或接口
    // todo 重写
    public static Azimuth varifyRalation(GeoNode from, GeoNode to){
        Envelope envelope = to.getGeometry().getEnvelopeInternal();
        Coordinate c = from.getCenter();
        int[] angle = new int[4];
        angle[0] = (int) Math.round(Math.atan((envelope.getMinY() - c.y) / (envelope.getMinX() - c.x)) / Math.PI * 180);
        angle[0] = angleAbs(angle[0], c.x, c.y, envelope.getMinX(), envelope.getMinY());

        angle[1] = (int) Math.round(Math.atan((envelope.getMaxY() - c.y) / (envelope.getMinX() - c.x))/ Math.PI * 180);
        angle[1] = angleAbs(angle[1], c.x, c.y, envelope.getMinX(), envelope.getMaxY());

        angle[2] = (int) Math.round(Math.atan((envelope.getMinY() - c.y) / (envelope.getMaxX() - c.x)) / Math.PI * 180);
        angle[2] = angleAbs(angle[2], c.x, c.y, envelope.getMaxX(), envelope.getMinY());

        angle[3] = (int) Math.round(Math.atan((envelope.getMaxY() - c.y) / (envelope.getMaxX() - c.x)) / Math.PI * 180);
        angle[3] = angleAbs(angle[3], c.x, c.y, envelope.getMaxX(), envelope.getMaxY());

        // System.out.println(envelope.getMinY() - c.y / envelope.getMinX() - c.x);
//        System.out.println(" xxxx  " + (envelope.getMaxX() - c.x));
//        System.out.println(" xxxx  " + (envelope.getMinX() - c.x));
//        System.out.println(" xxxx  " + (envelope.getMaxY() - c.y));
//        System.out.println(" xxxx  " + (envelope.getMinY() - c.y));
        Arrays.sort(angle);
//        System.out.println(Arrays.toString(angle));
        return new Azimuth((angle[0] + angle[1]) / 2, (angle[2] + angle[3]) / 2);
    }


    /**
     * 是否是正序  g1 在 g2 左边
     * @param g1
     * @param g2
     * @return
     */
    public static boolean positiveSeq(GeoNode g1, GeoNode g2) {
        return g1.getCenter().x < g2.getCenter().x;
    }

    /**
     * 根据象限确认角度
     * @return
     */
    private static int angleAbs(int angle, double x1, double y1, double x2, double y2) {
        // 一象限
        if (x1 <= x2 && y1 <= y2) {
            return angle;
        }
        // 2
        if (x1 >= x2 && y1 <= y2) {
            return 180 + angle;
        }
        // 3
        if (x1 >= x2 && y1 >= y2) {
            return -180 + angle;
        }
        // 4
        if (x1 <= x2 && y1 >= y2) {
            return angle;
        }
        return angle;

    }

}
