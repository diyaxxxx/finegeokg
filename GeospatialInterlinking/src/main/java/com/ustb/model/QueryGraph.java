package com.ustb.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author weib
 * @date 2023-02-27 17:06
 * 查询图
 * 邻接表表示
 */
public class QueryGraph {

    public List<Vertex> vs = new ArrayList<>();

    private Map<Integer, Vertex> map= new HashMap<>();

    public void createVertex(int id, int type) {
        Vertex v = new Vertex(id, type, null);
        vs.add(v);
        map.put(id, v);
    }

    public void createEdge(int eId, int fId, int tId, int angle, double length) {
        Vertex vf = map.get(fId);
        Vertex vt = map.get(tId);
        Property property = new Property(angle, length);
        vf.e = new Edge(eId, vt, property, vf.e);
    }



    @Data
    @AllArgsConstructor
    public class Edge {
        public int id;
        public Vertex v;
        public Property property;
        public Edge nextEdge;
    }
    @Data
    @AllArgsConstructor
    public class Vertex {
        public int id;
        public int type;
        public Edge e;
    }
    @Data
    @AllArgsConstructor
    public class Property{
        public int angle;
        public double length;
    }


}
