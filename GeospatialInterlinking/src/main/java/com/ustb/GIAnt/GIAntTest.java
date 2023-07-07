package com.ustb.GIAnt;

import com.ustb.common.Constant;
import org.locationtech.jts.io.ParseException;

import java.io.IOException;

public class GIAntTest {

    public static void main(String[] args) throws ParseException, IOException {
        String mainDir = Constant.DATASETDIR;
        String delimiter = "\"";
        GIAnt giant = new GIAnt(2401396, delimiter, mainDir + "osm1k.csv", mainDir + "osm1k.csv");
        giant.applyProcessing();
        giant.printResults();
    }
}
