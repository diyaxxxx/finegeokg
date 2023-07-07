package com.ustb.fsgr;


import com.ustb.common.Constant;

import java.io.IOException;

/**
 * @author weib
 * @date 2022-05-28 16:37
 * 关系查找测试(连边)
 */
public class RFTest {

    public static void main(String[] args) throws IOException {
        String mainDir = Constant.DATASETDIR;
        String file = mainDir + "osm1k.csv";

        String delimiter = "\"";
        GeoSpatialRelation.sgrDis = 1.0D;
        GrgCreator.testCreateAndVirify(delimiter, file);
        GrgCreator.print();
    }
}
