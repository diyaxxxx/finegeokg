package com.ustb.model;

import java.io.Serializable;

/**
 * @author weib
 * @date 2022-04-03 21:19
 * 拓扑关系
 */
public enum TopoEnum implements Serializable {
    /**
     *
     */
    SEPARATED(1),
    CONTAINS(2),
    COVEREDBY(3),
    COVERS(4),
    CROSSES(5),
    EQUALS(6),
    OVERLAPS(7),
    TOUCHES(8),
    WITHIN(9),
    INTERSECTS(10);
    public int code;
    TopoEnum(int code) {
        this.code = code;
    }
}
