package com.ustb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author weib
 * @date 2022-04-12 21:25
 * 方位角
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Azimuth implements Serializable {
    private int low;
    private int high;
    private int span;

    public Azimuth(int low, int high) {
        this.low = low;
        this.high = high;
        span = high - low;
    }
}
