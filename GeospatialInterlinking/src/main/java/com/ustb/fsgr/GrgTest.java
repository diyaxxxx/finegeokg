package com.ustb.fsgr;

import com.ustb.common.Constant;
import com.ustb.fsgr.find_v1.FSGR_find;
import com.ustb.fsgr.find_v1.index_v1.*;
import com.ustb.fsgr.find_v2.FSGR_find_pro;
import com.ustb.fsgr.find_v2.FindMap;
import com.ustb.model.GeoDataSet;
import com.ustb.model.GeoNode;
import com.ustb.util.CsvReader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author weib
 * @date 2022-04-05 23:48
 * 测试
 */
public class GrgTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        String file = Constant.DATASETDIR + "osm1w.csv";
        String delimiter = "\"";
        int count = 0;

        GeoSpatialRelation.sgrDis = 1.0D;
        FindMap findMap = new FindMap();

        findMap.addNode(2, 2,0);
        findMap.addNode(2, 2,1);
        findMap.addNode(2, 2,2);
        findMap.addNode(2,2,3);
        findMap.addNode(2, 2,4);

        findMap.addRelation(0, 1,0, 30, 0, 5);
        findMap.addRelation(0, 2,-1, 60, -1, 5);
        findMap.addRelation(0, 3,0, 30, 0, 5);
        findMap.addRelation(1, 2,-1, 60, -1, 5);
        findMap.addRelation(1, 4,0, 30, 0, 5);
        findMap.addRelation(2, 3,-1, 60, -1, 5);
        findMap.addRelation(2, 4,0, 30, 0, 5);
        findMap.addRelation(3, 4,-1, 60, -1, 5);


        List<GeoSpatialRelation> gsr = GrgCreator.getGraph(delimiter, file);
        System.out.println("------" + gsr.size());


        FSGR_find_pro findPro = new FSGR_find_pro();
        long beginTime = System.currentTimeMillis();
        List<Map<Integer, GeoNode>> result = findPro.find(findMap, gsr);
        for (Map<Integer, GeoNode> m : result) {
            List<GeoNode> ns = new ArrayList<>(m.values());
            for (GeoNode n : ns) {
                System.out.print(n.getId() + " ") ;
                count++;
            }
            System.out.println();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("count:"+count);
        System.out.println("总时间："+(endTime-beginTime)+"ms");



//        for (GeoSpatialRelation gsr : gsrs) {
//            System.out.print(gsr.getFrom().getGeoInfo().getName());
//            System.out.print(" ---> ");
//            System.out.print(gsr.getTo().getGeoInfo().getName());
//            System.out.print(" 方位角：<" + gsr.getAzimuth().getLow());
//            System.out.print("," + gsr.getAzimuth().getHigh() + ">");
//            System.out.print("  相接长度" + gsr.getTouchLength());
//            System.out.println();
//        }

//        for (String s : gsrs3) {
//            System.out.println(s);
//        }


    }

    public static int count12 = 0;

    public static void main2(String[] args) throws IOException, ClassNotFoundException {

        String file = Constant.DATASETDIR + "osm100w.csv";
        String delimiter = "\"";

        GeoSpatialRelation.sgrDis = 1.0D;
        GeoDataSet dataSet = CsvReader.readDataSet(delimiter, file);
        List<GeoSpatialRelation> gsr = GrgCreator.getGraph(delimiter, file);

        List<FSGR_find.Condition> cs = new ArrayList<>();
        // 点线面 123
        // 3-8  2-4  1-11
        FSGR_find.Condition c1 = new FSGR_find.Condition(23, 21310, 2, 3, -1, 60, 30, -1, -1,11,1);
        FSGR_find.Condition c2 = new FSGR_find.Condition(13, 17310, 1, 3, -1, 90, 30, -1, -1,12, 1);
        cs.add(c1);
        cs.add(c2);
        List<List<GeoSpatialRelation>> result = new FSGR_find(gsr, dataSet).find(cs);
//        int top = 20;
        String filePath = "C:\\Users\\wwei\\Desktop\\test.txt";
        Writer w = new FileWriter(filePath);
        for (List<GeoSpatialRelation> gs : result) {
            w.write(gs.toString());
            w.write("\n\n\n");
        }
        w.flush();
        w.close();

        System.out.println(result);

    }








    /**
     * 查找时间测试
     */
    public static void findTest(int[] ms, int[] step, int length, Index index){
        // 测试查找时间
        // 步长
        for (int s : step) {
            System.out.println("步长：" + s);
            // 跨度
            for (int m : ms) {
                long t0 = System.currentTimeMillis();

                for (int i = 0; i < 10; i++) {
                    for (int start = -180; start + m <= 180; start += s) {
                        index.find(start, start+m, length, 10000, false);
                    }
                }
                long t1 = System.currentTimeMillis();
                System.out.println("步长" + s + " 角度跨度 " + m + "  时间" +(t1-t0) + "ms");
            }
        }


    }


}
