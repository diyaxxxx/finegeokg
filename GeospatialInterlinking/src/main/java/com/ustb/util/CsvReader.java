package com.ustb.util;

import com.ustb.model.GeoDataSet;
import com.ustb.model.GeoNode;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public static final WKTReader WKT_READER = new WKTReader();
    public static long idCraetor = 0L;

    public static Geometry[] readAllEntities(String delimiter, String inputFilePath) throws IOException {
        final List<Geometry> loadedEntities = new ArrayList<>();
        final BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
        String line = reader.readLine();
        while (line != null) {
            String[] tokens = line.split(delimiter);
            Geometry geometry = null;
            try {
                if (2 <= tokens.length) {
                    geometry = WKT_READER.read(tokens[1].trim());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (geometry != null) {
                loadedEntities.add(geometry);
            }

            line = reader.readLine();
        }
        reader.close();
        return loadedEntities.toArray(new Geometry[loadedEntities.size()]);
    }

    public static GeoDataSet readDataSet(String delimiter, String inputFilePath) throws IOException {
        idCraetor = 0L;
        final List<Geometry> loadedEntities = new ArrayList<>();
        final BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
        GeoDataSet geoDataSet = null;
        List<GeoNode> geoNodes = new ArrayList<>();
        double x = 0d;
        double y = 0d;
        int countPolygon = 0;
        String line = reader.readLine();
        while (line != null) {
            GeoNode node = null;
            try {
                // todo "\t" 提出去
                node = readGeoNode(line, delimiter, ",");
                // 面才+ x  y
                // todo 3 表示面 点线面 123
                    x += node.getGeometry().getEnvelopeInternal().getWidth();
                    y += node.getGeometry().getEnvelopeInternal().getHeight();
                    countPolygon++;

            } catch (Exception ex) {
                System.out.println(idCraetor);
                ex.printStackTrace();
            }
            if (node != null) {
                geoNodes.add(node);
            }
            line = reader.readLine();
        }
        reader.close();
        return new GeoDataSet(geoNodes, x/geoNodes.size(), y/geoNodes.size());
    }

    /**
     * # 附加信息 id 类型id 类型描述 图形类型：点线面 123
     * @param dataStr   一行数据
     * @param delimiter  多边形与描述信息之间的分隔符
     * @param desDelimiter  描述信息的分隔符
     * @return
     */
    private static GeoNode readGeoNode(String dataStr, String delimiter, String desDelimiter) throws ParseException {
        String[] tokens = dataStr.split(delimiter);
        Geometry geometry = null;
        GeoNode.GeoInfo geoInfo = null;
        if (3 <= tokens.length) {
            geometry = WKT_READER.read(tokens[1].trim());
            // 5 地名
//            geoInfo = new GeoNode.GeoInfo(tokens[1], tokens[2].split(desDelimiter)[5], tokens[2]);
            String name = "null";
            int geoType = 0;
            int detailType = 0;
            double length = -1D;
            double area = -1D;
            try {
                String[] infos = tokens[2].split(desDelimiter);
                geoType = Integer.parseInt(infos[1].trim());
                detailType = Integer.parseInt(infos[2].trim());
            }
            catch (Exception e){
                System.out.print("INFO : 详情信息(后缀)越界");
            }
            geoInfo = new GeoNode.GeoInfo(null, null, geoType, detailType, -1, -1);
        }
        if (geometry != null) {
            return new GeoNode(idCraetor++, geometry, geoInfo);
        }
        return null;
    }

}
