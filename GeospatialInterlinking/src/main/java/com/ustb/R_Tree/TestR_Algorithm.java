package com.ustb.R_Tree;

import com.ustb.common.Constant;

import java.io.IOException;

/**
 * @author weib
 * @date 2022-03-05 17:25
 */
public class TestR_Algorithm {
    public static void main(String[] args) throws IOException {
        String mainDir = Constant.DATASETDIR;
        String delimiter = "\"";
        String[] dataset1 = {"osm1k.csv"};
        String[] dataset2 = {"osm1k.csv"};
        R_Algorithm r= new R_Algorithm(delimiter, mainDir+dataset1[0], mainDir+dataset2[0]);
        r.exec();
        r.printResult();
    }
}
