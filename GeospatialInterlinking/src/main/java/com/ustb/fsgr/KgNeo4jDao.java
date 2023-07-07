package com.ustb.fsgr;

import com.ustb.model.GeoNode;
import org.neo4j.driver.v1.*;

import java.util.List;

/**
 * @author weib
 * @date 2022-04-06 0:10
 * 数据库访问对象
 * https://jishuin.proginn.com/p/763bfbd720f2 改进
 */
public class KgNeo4jDao {
    private static final Driver DRIVER =
            GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "root" ) );

    private static final Session SESSION = DRIVER.session();
    public static void addNode(GeoNode node) {

        String cql = "create (n : GeoNode {id: {id}, polygon :{polygon}, name:{name}, des:{des}})";
        SESSION.run(cql, Values.parameters("id", node.getId(),
                "polygon", null,
                "name", node.getGeoInfo().getName(),
                "des", node.getGeoInfo().getDes()));
    }


    public static void addRelation(GeoSpatialRelation sr){
        String cql = "match (n1:GeoNode{id:{id1}}), (n2:GeoNode{id:{id2}}) with n1, n2 " +
                "create (n1)-[:sr{topo:{topo}, direction:{direction}, distance:{distance}}]->(n2)";
        SESSION.run(cql, Values.parameters("id1", sr.getFrom().getId(),
                "id2", sr.getTo().getId(),
                "topo", sr.getTopo().code,
                "direction", sr.getDirection().code,
                "distance", sr.getLength()));
    }

    public static void addRelation(List<GeoSpatialRelation> srs){
        for (GeoSpatialRelation sr : srs) {
            addRelation(sr);
        }
    }


    public static void close(){
        SESSION.close();
        DRIVER.close();
    }

}
