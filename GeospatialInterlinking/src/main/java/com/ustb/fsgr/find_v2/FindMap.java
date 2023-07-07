package com.ustb.fsgr.find_v2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weib
 * @date 2023-02-23 9:50
 */
@Data
public class FindMap {
    @AllArgsConstructor
    @Data
    public static class Node{
        int type; //点线面 123 具体的12  23  31  -1表示无
        int detailType; //具体类型  -1表示无
        int order;//顺序
    }
    @AllArgsConstructor
    @Data
    public static class Relation{
        Node from;
        Node to;
        int aStart; //角度开始  -1 表示无
        int aEnd; //角度结束   -1表示无
        double lStart; //长度开始 -1表示无
        double lEnd; //长度结束 -1表示无
    }

    private List<Node> nodes = new ArrayList<>();
    private List<Relation> relations = new ArrayList<>();

    public void addNode(int type, int detailType, int order){
        Node node = new Node(type, detailType, order);
        nodes.add(node);
    }

    /**
     *
     * @param from  第几个node  从0开始
     * @param to  第几个node  从0开始
     */
    public void addRelation(int from, int to, int aStart, int aEnd, double lStart, double lEnd){
        Relation r = new Relation(nodes.get(from), nodes.get(to), aStart, aEnd, lStart, lEnd);
        relations.add(r);
    }

}
























