package com.ustb.fsgr;

import com.ustb.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Random;

/**
 * @author weib
 * @date 2022-04-03 22:06
 * 空间关系
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GeoSpatialRelation implements Serializable {
    // 认定为强空间关系的范围
    public static double sgrDis = 1.0D;

    private long id;
    private GeoNode from;
    private GeoNode to;
    private TopoEnum topo;
    private DirectionEnum direction;
    // 面-面 方位角
    private Azimuth azimuth;
    // 面-面 相接长度,其他长度
    private double length;
    // 边类型 12 13 点线  点面
    private int gsrGeoType;
    // 边类型 1214 1238
    private int gsrDetailType;
    // 其他五种角度
    private int angle;


    // todo  注意调用顺序影响结果
    public static GeoSpatialRelation varifyRelation(GeoNode from, GeoNode to) {
        //  from 左  to 右
        if (!DirectionValidator.positiveSeq(from, to)) {
            GeoNode t = from;
            from = to;
            to = t;
        }
        int[] type = getGsrType(from, to);
        final TopoEnum topo = RelatedGeometries.verifyRelations(from, to);
        double length = getSgrLength(from, to);
        // 只有点-点  点-线可以相离, 点线的强空间关系条件
        if (type[0] == 12) {
            GrgTest.count12++;
        }
        if (topo == TopoEnum.SEPARATED) {
            if (type[0] != 11 && type[0] != 12) {
                return null;
            }
            if (length > sgrDis) {
                return null;
            }
        }
        Azimuth azimuth = null;
        if (type[0] == 33) {
            azimuth =  DirectionValidator.varifyRalation(from, to);
//            length = TouchCalcor.calc(from.getGeometry(), to.getGeometry());
        }
        return new GeoSpatialRelation(getUid(), from, to, topo, null, azimuth, length, type[0], type[1], getsgrAngle(from, to));
    }

    private static long count = 0;
    private static long getUid () {
        return count++;
    }

    private static Random rCreator = new Random(1);


    private static int getsgrAngle(GeoNode n1, GeoNode n2){
        int a = 0;
        try {
            a = Math.abs((int)(n2.getGeometry().getEnvelopeInternal().getMaxX() - n1.getGeometry().getEnvelopeInternal().getMaxX()) / (int)(n2.getGeometry().getEnvelopeInternal().getMaxY() - n1.getGeometry().getEnvelopeInternal().getMaxY()));
        } catch (Exception e){
            return 0;
        }
        return a;
    }


    private static double getSgrLength(GeoNode n1, GeoNode n2) {
        if (isP_P(n1, n2) || isP_PL(n1, n2)) {
            return TouchCalcor.calc(n1.getGeometry(), n2.getGeometry());
        }
        return n1.getGeometry().distance(n2.getGeometry());
    }

    /**
     * 获取边类型  返回一个数组  里面两个元素 第一个是12 13 等点线类型，第二个是 小学-林地 具体类型
     * @param n1
     * @param n2
     * @return
     */
    private static int[] getGsrType(GeoNode n1, GeoNode n2) {
        int[] result = new int[2];
        StringBuilder sb = new StringBuilder();
        int gt = n1.getGeoInfo().getGeoType();
        int dt = n1.getGeoInfo().getDetailType();
        int n1T = Integer.parseInt(gt + "" + dt);

        int gt2 = n2.getGeoInfo().getGeoType();
        int dt2 = n2.getGeoInfo().getDetailType();
        int n2T = Integer.parseInt(gt2 + "" + dt2);

        if (gt > gt2) {
            int t = gt;
            gt = gt2;
            gt2 = t;
        }
        result[0] = Integer.parseInt(gt + "" + gt2);
        // 保证 n1 是小于 n2的，为了统一
        if (n1T > n2T) {
            int t = n1T;
            n1T = n2T;
            n2T = t;
        }
        result[1] = Integer.parseInt(n1T + "" + n2T);
        return result;
    }



    /**
     * 两个面
     */
    private static boolean isP_P (GeoNode n1, GeoNode n2) {
        if (n1.getGeoInfo().getGeoType() == 3 && n2.getGeoInfo().getGeoType() == 3){
            return true;
        }
        return false;
    }

    /**
     * 线 面
     * @param n1
     * @param n2
     * @return
     */
    private static boolean isP_PL (GeoNode n1, GeoNode n2) {
        if (n1.getGeoInfo().getGeoType() == 2 && n2.getGeoInfo().getGeoType() == 3){
            return true;
        }
        if (n1.getGeoInfo().getGeoType() == 3 && n2.getGeoInfo().getGeoType() == 2){
            return true;
        }
        return false;
    }
}
