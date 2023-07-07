package com.ustb.fsgr.find_v1.index_v1;

import com.ustb.model.GeoNode;

import java.util.List;
import java.util.TreeSet;

public class AreaIndex {
    BTree<GeoNode, GeoNode> index;

    public AreaIndex(List<GeoNode> nodes) {
        index = new BTree<>(getTreeSet());
        for (GeoNode node : nodes) {
            if (node.getGeoInfo().getArea() > 0) {
                index.add(node);
            }
        }
    }

    public List<GeoNode> before(int key) {
        return index.before(sortedObject(key));
    }
    public List<GeoNode> after(int key) {
        return index.after(sortedObject(key));
    }
    public List<GeoNode> sub(int k1, int k2) {
        return index.subSet(sortedObject(k1), sortedObject(k2));
    }

    private TreeSet<GeoNode> getTreeSet() {
        return new TreeSet<GeoNode>((a, b) -> {
            if (a.getGeoInfo().getArea() == b.getGeoInfo().getArea())   {
                return (int) (b.getId() - a.getId());
            }
            // 放大
            return (int) (a.getGeoInfo().getArea() - b.getGeoInfo().getArea())*1000;
        });
    }

    private GeoNode sortedObject(double area) {
        GeoNode node = new GeoNode();
        node.setId(-1L);
        node.setGeoInfo(new GeoNode.GeoInfo());
        node.getGeoInfo().setArea(area);
        return node;
    }
}
