package com.ustb.fsgr;

import com.ustb.fsgr.find_v1.index_v1.AzimuthIndex;
import com.ustb.fsgr.find_v1.index_v1.NormalIndex;
import com.ustb.fsgr.find_v1.index_v1.SixIndex;
import com.ustb.model.GeoDataSet;
import com.ustb.model.GeoNode;
import com.ustb.model.LightIndex;
import com.ustb.model.RelatedGeometries;
import com.ustb.util.CsvReader;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.locationtech.jts.geom.Envelope;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author weib
 * @date 2022-03-31 14:41
 * 地理关系图生成器
 */
public class GrgCreator {

    public static RelatedGeometries relatedGeometries = new RelatedGeometries(1);

    public static void print(){
        relatedGeometries.print();
    }

    /**
     * 创建 方位索引
     * @return
     * @throws IOException
     */
    public static AzimuthIndex createAziIndex(List<GeoSpatialRelation> relations)  {
        AzimuthIndex azimuthIndex = new AzimuthIndex();
        System.out.println("Azimuth index_v1");
        long t1 = System.currentTimeMillis();
        azimuthIndex.addAll(relations);
        long t2 = System.currentTimeMillis();
        System.out.println("索引建立时间 " + (t2 - t1) + "ms");
//        KgNeo4jDao.close();
        return azimuthIndex;
    }

    public static NormalIndex createNormalInde(List<GeoSpatialRelation> relations)  {
        NormalIndex normalIndex = new NormalIndex();
        System.out.println("normal index_v1");
        // 建索引
        normalIndex.add(relations);
        return normalIndex;
    }

    public static SixIndex createSixIndex(List<GeoSpatialRelation> relations)  {
        System.out.println("six index_v1");
        // 建索引
        SixIndex sixIndex = new SixIndex(relations.size());
        long t1 = System.currentTimeMillis();
        sixIndex.add(relations);
        long t2 = System.currentTimeMillis();
        System.out.println("索引建立时间 " + (t2 - t1) + "ms");
        return sixIndex;
    }


    public static List<GeoSpatialRelation> getGraph(String delimiter, String inputFilePath) throws IOException, ClassNotFoundException {
        System.out.println("读取反序列化文件！");
        File file = new File(inputFilePath + ".objs");
        List<GeoSpatialRelation> result = new ArrayList<>();
        if (file.exists()) {
            System.out.println("反序列化！");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            result = (List<GeoSpatialRelation>) ois.readObject();
            ois.close();
            return result;
        }
//        ObjectInputStream
        System.out.println("反序列化文件不存在，读取源数据集！");
        GeoDataSet dataSet = CsvReader.readDataSet(delimiter, inputFilePath);
        System.out.println("数据集节点数：" + dataSet.getGeoNodes().size());
        LightIndex spatialIndex = new LightIndex();
        for (GeoNode geoNode : dataSet.getGeoNodes()) {
            TIntSet candidateMatches = findAndSave(spatialIndex, geoNode, dataSet.getAverageX(), dataSet.getAverageY());
            result.addAll(calcRelation(candidateMatches, dataSet, geoNode));
        }
        System.out.println("创建序列化文件");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(result);
        oos.close();
        return result;
    }

    /**
     * 在索引中寻找“附近”的对象  并把当前对象存入索引
     * @param spatialIndex
     * @param node
     * @return
     */
    private static TIntSet findAndSave(LightIndex spatialIndex, GeoNode node, double ax, double ay){
        Envelope envelope = node.getGeometry().getEnvelopeInternal();
        int maxX = (int) Math.ceil(envelope.getMaxX() / ax);
        int maxY = (int) Math.ceil(envelope.getMaxY() / ay);
        int minX = (int) Math.floor(envelope.getMinX() / ax);
        int minY = (int) Math.floor(envelope.getMinY() / ay);
        // todo TIntSet 改成常规的吧
        final TIntSet candidateMatches = new TIntHashSet();
//        System.out.println(minX + "--》" + maxX);
//        System.out.println(minY + "--》" + maxY);
        if (maxX - minX > 50 || maxY - minY > 50) {
            return null;
        }
        for (int latIndex = minX; latIndex <= maxX; latIndex++) {
            for (int longIndex = minY; longIndex <= maxY; longIndex++) {
                final TIntList partialCandidates = spatialIndex.getSquare(latIndex, longIndex);
                if (partialCandidates != null) {
                    candidateMatches.addAll(partialCandidates);
                }
                // 存入
                spatialIndex.add(latIndex, longIndex, node.getId().intValue());
            }
        }
        return candidateMatches;
    }


    /**
     * 计算关系
     * @param candidateMatches
     * @param dataSet
     * @param node
     * @return
     */
    private static List<GeoSpatialRelation> calcRelation(TIntSet candidateMatches, GeoDataSet dataSet, GeoNode node) {
        if (candidateMatches == null) {
            return new ArrayList<>();
        }
        final TIntIterator intIterator = candidateMatches.iterator();
        final List<GeoNode> geoNodes = dataSet.getGeoNodes();
        List<GeoSpatialRelation> relations = new ArrayList<>();
        while (intIterator.hasNext()) {
            int id = intIterator.next();

           // relatedGeometries.verifyRelations(id, node.getId().intValue(), geoNodes.get(id).getGeometry(), node.getGeometry());
            try {
                GeoSpatialRelation sr = GeoSpatialRelation.varifyRelation(geoNodes.get(id), node);
                if (sr != null) {
                    relations.add(sr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return relations;
    }


    private static void testVarify(TIntSet candidateMatches, GeoDataSet dataSet, GeoNode node) {
        if (candidateMatches == null) {
            return;
        }
        final TIntIterator intIterator = candidateMatches.iterator();
        final List<GeoNode> geoNodes = dataSet.getGeoNodes();
        while (intIterator.hasNext()) {
            int id = intIterator.next();
            relatedGeometries.verifyRelations(id, node.getId().intValue(), geoNodes.get(id).getGeometry(), node.getGeometry());
        }
    }

    /**
     * 验证创建索引和验证关系 时间
     */
    public static void testCreateAndVirify(String delimiter, String inputFilePath) throws IOException {
        GeoDataSet dataSet = CsvReader.readDataSet(delimiter, inputFilePath);
        AzimuthIndex azimuthIndex = new AzimuthIndex();
        System.out.println("读完成！");
        // 建索引
        long t1 = System.currentTimeMillis();
        LightIndex spatialIndex = new LightIndex();
        for (GeoNode geoNode : dataSet.getGeoNodes()) {
            TIntSet candidateMatches = findAndSave(spatialIndex, geoNode, dataSet.getAverageX(), dataSet.getAverageY());
            testVarify(candidateMatches, dataSet, geoNode);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("索引建立 + 验证时间 ：" + (t2 - t1) + "ms");
    }

}