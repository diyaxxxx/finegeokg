package com.ustb.model;

import java.io.Serializable;

/**
 * @author weib
 * @date 2022-04-03 21:46
 * 方位关系
 */
public enum DirectionEnum implements Serializable {
    /**
     *
     */
    East(1),
    South(2), West(3), North(4), Southeast(5), Southwest(6), Northeast(7), Northwest(8);

    public int code;
    DirectionEnum(int code) {
        this.code = code;
    }

    /**
     * 根据code查找
     * @param code 枚举code
     * @return 枚举对象
     */
    public static DirectionEnum findEnumByCode(int code) {
        for (DirectionEnum statusEnum : DirectionEnum.values()) {
            if (statusEnum.code == code) {
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("code is invalid");
    }

}
