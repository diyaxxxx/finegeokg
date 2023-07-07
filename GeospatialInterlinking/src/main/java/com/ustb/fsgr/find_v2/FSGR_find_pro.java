package com.ustb.fsgr.find_v2;

import com.ustb.fsgr.GeoSpatialRelation;
import com.ustb.fsgr.find_v2.index.SgrIndex;
import com.ustb.model.GeoNode;
import com.ustb.model.SgrType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

/**
 * @author weib
 * @date 2023-02-23 9:50
 *
 */
public class FSGR_find_pro {

    //优先级
    @AllArgsConstructor
    @Data
    class PriorityRelation{
        FindMap.Relation r;
        int p; // 2 1 0
    }

    // 点是否访问过
    Map<FindMap.Node, Boolean> visitMap = new HashMap<>();

    public List<Map<Integer, GeoNode>> find(FindMap findMap, List<GeoSpatialRelation> gsrs){
        List<Map<Integer, GeoNode>> resultT = new ArrayList<>();
        // 创建索引
        SgrIndex index = new SgrIndex();
        index.build(gsrs);
        // 创建优先级序列 最开始都是2
        List<PriorityRelation> priorityRelations = new ArrayList<>();
        for (FindMap.Relation relation : findMap.getRelations()) {
            PriorityRelation priorityRelation = new PriorityRelation(relation, 2);
            priorityRelations.add(priorityRelation);
        }
        // 初始化visitMap
        for (FindMap.Node node : findMap.getNodes()) {
            visitMap.put(node, false);
        }
        // 开始查询
        for (int i = 0; i < findMap.getRelations().size(); i++) {
            // 获取第一个
            PriorityRelation relation  = priorityRelations.get(0);
            visitMap.put(relation.r.from, true);
            visitMap.put(relation.r.to, true);
            sortPriorityRelations(priorityRelations);
            int typeCode = getTypeCode(relation.r.from.type, relation.r.to.type);
            SgrType sgrType = SgrType.getType(typeCode);
            List<Map<Integer, GeoNode>> tbl1 = getTBL(resultT, relation.r.from.order);
            List<Map<Integer, GeoNode>> tbl2 = getTBL(resultT, relation.r.to.order);
            if (tbl1.size() == 0 && tbl2.size() == 0) {
                List<GeoSpatialRelation> rs = index.find(typeCode, relation.r.aStart, relation.r.aEnd, relation.r.lStart, relation.r.lEnd);
                addResult(resultT, rs, relation.r);
                continue;
            }
            if (tbl1.size() > 0 && tbl2.size() == 0) {
                List<GeoSpatialRelation> rs = index.find(typeCode, relation.r.aStart, relation.r.aEnd, relation.r.lStart, relation.r.lEnd);
                resultT = joinResult(tbl1, rs, relation.r);
                continue;
            }
            if (tbl1.size() == 0 && tbl2.size() > 0) {
                List<GeoSpatialRelation> rs = index.find(typeCode, relation.r.aStart, relation.r.aEnd, relation.r.lStart, relation.r.lEnd);
                resultT = joinResult(tbl2, rs, relation.r);
                continue;
            }else {
                resultT = joinTables(resultT, relation.r, index);
            }

        }
        return resultT;
    }

    private void addResult(List<Map<Integer, GeoNode>> resultT, List<GeoSpatialRelation> rs, FindMap.Relation r){
        int fromType = r.from.type;
        for (GeoSpatialRelation gsr : rs) {
            Map<Integer, GeoNode> item = new HashMap<>();
            if (gsr.getFrom().getGeoInfo().getGeoType() == fromType) {
                item.put(r.from.order, gsr.getFrom());
                item.put(r.to.order, gsr.getTo());
                resultT.add(item);
                continue;
            }
            item.put(r.from.order, gsr.getTo());
            item.put(r.to.order, gsr.getFrom());
            resultT.add(item);
        }
    }


    private List<Map<Integer, GeoNode>> joinResult(List<Map<Integer, GeoNode>> resultT, List<GeoSpatialRelation> rs, FindMap.Relation r){
        int targetOrder = 0;
        int exsitOrder = 0;
        boolean addFrom = false;
        if (resultT.get(0).get(r.from.order) != null) {
            exsitOrder = r.from.order;
            targetOrder = r.to.order;
        }else {
            exsitOrder = r.to.order;
            targetOrder = r.from.order;
            addFrom = true;
        }
        List<Map<Integer, GeoNode>> newResultT = new ArrayList<>();
        if (!addFrom) {
            rs.sort((a, b) -> (int) (a.getFrom().getId() - b.getFrom().getId()));
        }else {
            rs.sort((a, b) -> (int) (a.getTo().getId() - b.getTo().getId()));
        }
        for (Map<Integer, GeoNode> item : resultT) {
            if (!addFrom) {
                boolean ing = false;
                for (GeoSpatialRelation gsr : rs) {
                    if (ing && gsr.getFrom().getId() != item.get(exsitOrder).getId()) {
                        break;
                    }
                    if (gsr.getFrom().getId() == item.get(exsitOrder).getId()) {
                        ing = true;
                        Map map = new HashMap(item);
                        map.put(targetOrder, gsr.getTo());
                        newResultT.add(map);
                    }
                }
            }else {
                boolean ing = false;
                for (GeoSpatialRelation gsr : rs) {
                    if (ing && gsr.getTo().getId() != item.get(exsitOrder).getId()) {
                        break;
                    }
                    if (gsr.getTo().getId() == item.get(exsitOrder).getId()) {
                        ing = true;
                        Map map = new HashMap(item);
                        map.put(targetOrder, gsr.getFrom());
                        newResultT.add(map);
                    }
                }
            }
        }
        return newResultT;
    }

    private List<Map<Integer, GeoNode>> getTBL(List<Map<Integer, GeoNode>> resultT, int order) {
        if (resultT.size() == 0) {
            return new ArrayList<>();
        }
        if (resultT.get(0).get(order) == null) {
            return new ArrayList<>();
        }
        return resultT;
    }

    private int getTypeCode(int g1, int g2) {
        if (g1 <= g2) {
            return Integer.parseInt(g1 + "" + g2);
        }
        return Integer.parseInt(g2 + "" + g1);
    }

    private List<Map<Integer, GeoNode>> joinTables(List<Map<Integer, GeoNode>> resultT, FindMap.Relation r, SgrIndex index){
        List<Map<Integer, GeoNode>> result = new ArrayList<>();
        for (Map<Integer, GeoNode> re : resultT) {
            Long from = re.get(r.from.order).getId();
            Long to = re.get(r.to.order).getId();
            if (index.maightExsit(from, to, getTypeCode(r.from.type, r.to.type)))
            {
                result.add(re);
            }
        }
        return result;
    }

    private void sortPriorityRelations(List<PriorityRelation> priorityRelations) {
        for (PriorityRelation r : priorityRelations) {
            if (visitMap.get(r.r.from)) {
                r.p -= 1;
            }
            if (visitMap.get(r.r.to)) {
                r.p -= 1;
            }
        }
        priorityRelations.sort((a, b) -> b.p - a.p);
    }

}
