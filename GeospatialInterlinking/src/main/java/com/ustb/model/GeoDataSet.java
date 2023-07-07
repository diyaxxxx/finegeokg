package com.ustb.model;

import com.ustb.model.GeoNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weib
 * @date 2022-04-03 20:38
 * 数据集
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoDataSet {
    private List<GeoNode> geoNodes;
    private double averageX;
    private double averageY;

}
