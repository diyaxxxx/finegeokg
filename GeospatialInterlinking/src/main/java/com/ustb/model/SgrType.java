package com.ustb.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weib
 * @date 2022-07-06 14:23
 * 边类型
 */
public enum SgrType {
    /*
     1 2 3
    12 13 点线  点面
    * */
    POLY_POLY(33, "面-面"),
    POLY_LS(23, "面-线"),
    POLY_POINT(13, "面-点"),
    LS_LS(22, "线-线"),
    LS_POINT(12, "线-点"),
    POINT_POINT(11, "点-点");


    int id;
    String des;
    SgrType(int id, String des){
        this.id = id;
        this.des = des;
    }

    private static Map<Integer, SgrType> map = new HashMap<>();
    static {
        map.put(POLY_POLY.id, POLY_POLY);
        map.put(POLY_LS.id, POLY_LS);
        map.put(POLY_POINT.id, POLY_POINT);
        map.put(LS_LS.id, LS_LS);
        map.put(LS_POINT.id, LS_POINT);
        map.put(POINT_POINT.id, POINT_POINT);
    }

    public static SgrType getType(int code) {
        return map.get(code);
    }
}
