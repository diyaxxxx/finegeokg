package com.ustb.R_Tree;

import com.ustb.model.RelatedGeometries;
import com.ustb.util.CsvReader;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.index.strtree.STRtree;

import java.io.IOException;
import java.util.List;

/**
 * @author weib
 * @date 2022-03-05 17:14
 */
public class R_Algorithm {

    private Geometry[] sourceDataSet;
    private Geometry[] targetDataSet;
    private STRtree stRtree;
    RelatedGeometries relatedGeometries;

    /**
     * 索引时间
     */
    private long indexingTime;
    /**
     * 验证时间
     */
    private long verificationTime;


    public R_Algorithm(String delimiter, String sourceFilePath, String targetFilePath ) throws IOException {
        sourceDataSet = CsvReader.readAllEntities(delimiter, sourceFilePath);
        targetDataSet = CsvReader.readAllEntities(delimiter, targetFilePath);
        relatedGeometries = new RelatedGeometries(1);

    }
    public void exec(){
        long time1 = System.currentTimeMillis();
        createIndex();
        long time2 = System.currentTimeMillis();
        verification();
        long time3 = System.currentTimeMillis();
        indexingTime = time2 - time1;
        verificationTime = time3 - time2;
    }
    public void printResult(){
        System.out.println("R-tree 实现");
        System.out.println("索引建立时间：" + indexingTime + "ms");
        System.out.println("验证完成时间：" + verificationTime + "ms");
        relatedGeometries.print();
    }
    /**
     * 创建R-tree 索引
     */
    private void createIndex(){
        stRtree = new STRtree();
        for (int i = 0; i < sourceDataSet.length; i++) {
            Geometry geometry = sourceDataSet[i];
            stRtree.insert(geometry.getEnvelopeInternal(), geometry);
        }
    }

    private void verification(){
        int ti = 0;
        int si = 0;
        for (Geometry t : targetDataSet) {
            ti++;
            List<Geometry> sources = stRtree.query(t.getEnvelopeInternal());
            for (Geometry s : sources) {
                si++;
                if (s.getEnvelopeInternal().intersects(t.getEnvelopeInternal())) {
                    relatedGeometries.verifyRelations(si, ti, s, t);
                }
            }
        }
    }
}
