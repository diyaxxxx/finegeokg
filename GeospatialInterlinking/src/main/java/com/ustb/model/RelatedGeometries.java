package com.ustb.model;

import com.ustb.fsgr.GeoSpatialRelation;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.IntersectionMatrix;

public class RelatedGeometries {

    private int exceptions;
    private int detectedLinks;
    private int interlinkedGeometries;
    public final int qualifyingPairs;
    private int verifiedPairs;


    private final TIntList containsD1;
    private final TIntList containsD2;
    private final TIntList coveredByD1;
    private final TIntList coveredByD2;
    private final TIntList coversD1;
    private final TIntList coversD2;
    private final TIntList crossesD1;
    private final TIntList crossesD2;
    private final TIntList equalsD1;
    private final TIntList equalsD2;
    private final TIntList intersectsD1;
    private final TIntList intersectsD2;
    private final TIntList overlapsD1;
    private final TIntList overlapsD2;
    private final TIntList touchesD1;
    private final TIntList touchesD2;
    private final TIntList withinD1;
    private final TIntList withinD2;

    public RelatedGeometries(int qualifyingPairs) {
        exceptions = 0;
        detectedLinks = 0;
        verifiedPairs = 0;
        this.qualifyingPairs = qualifyingPairs;
        interlinkedGeometries = 0;

        containsD1 = new TIntArrayList();
        containsD2 = new TIntArrayList();
        coveredByD1 = new TIntArrayList();
        coveredByD2 = new TIntArrayList();
        coversD1 = new TIntArrayList();
        coversD2 = new TIntArrayList();
        crossesD1 = new TIntArrayList();
        crossesD2 = new TIntArrayList();
        equalsD1 = new TIntArrayList();
        equalsD2 = new TIntArrayList();
        intersectsD1 = new TIntArrayList();
        intersectsD2 = new TIntArrayList();
        overlapsD1 = new TIntArrayList();
        overlapsD2 = new TIntArrayList();
        touchesD1 = new TIntArrayList();
        touchesD2 = new TIntArrayList();
        withinD1 = new TIntArrayList();
        withinD2 = new TIntArrayList();
    }

    private void addContains(int gId1, int gId2) {
        containsD1.add(gId1);
        containsD2.add(gId2);
    }

    private void addCoveredBy(int gId1, int gId2) {
        coveredByD1.add(gId1);
        coveredByD2.add(gId2);
    }

    private void addCovers(int gId1, int gId2) {
        coversD1.add(gId1);
        coversD2.add(gId2);
    }

    private void addCrosses(int gId1, int gId2) {
        crossesD1.add(gId1);
        crossesD2.add(gId2);
    }

    private void addEquals(int gId1, int gId2) {
        equalsD1.add(gId1);
        equalsD2.add(gId2);
    }

    private void addIntersects(int gId1, int gId2) {
        intersectsD1.add(gId1);
        intersectsD2.add(gId2);
    }

    private void addOverlaps(int gId1, int gId2) {
        overlapsD1.add(gId1);
        overlapsD2.add(gId2);
    }

    private void addTouches(int gId1, int gId2) {
        touchesD1.add(gId1);
        touchesD2.add(gId2);
    }

    private void addWithin(int gId1, int gId2) {
        withinD1.add(gId1);
        withinD2.add(gId2);
    }

    public int getInterlinkedPairs() {
        return interlinkedGeometries;
    }

    private int getNoOfContains() {
        return containsD1.size();
    }

    private int getNoOfCoveredBy() {
        return coveredByD1.size();
    }

    private int getNoOfCovers() {
        return coversD1.size();
    }

    private int getNoOfCrosses() {
        return crossesD1.size();
    }

    private int getNoOfEquals() {
        return equalsD1.size();
    }

    private int getNoOfIntersects() {
        return intersectsD1.size();
    }

    private int getNoOfOverlaps() {
        return overlapsD1.size();
    }

    private int getNoOfTouches() {
        return touchesD1.size();
    }

    private int getNoOfWithin() {
        return withinD1.size();
    }

    public int getVerifiedPairs() {
        return verifiedPairs;
    }

    public void print() {
        System.out.println("总对数\t:\t" + qualifyingPairs);
        System.out.println("验证拓扑关系的对数\t:\t" + verifiedPairs);
        System.out.println("异常数\t:\t" + exceptions);
        System.out.println("包含关系数\t:\t" + getNoOfContains());
        System.out.println("被覆盖关系数:\t" + getNoOfCoveredBy());
        System.out.println("覆盖关系数\t:\t" + getNoOfCovers());
        System.out.println("交叉关系数\t:\t" + getNoOfCrosses());
        System.out.println("相等关系数\t:\t" + getNoOfEquals());
        System.out.println("相交关系数:\t" + getNoOfIntersects());
        System.out.println("部分重叠关系数\t:\t" + getNoOfOverlaps());
        System.out.println("相接关系数\t:\t" + getNoOfTouches());
        System.out.println("在内部关系数\t:\t" + getNoOfWithin());
    }



    // todo 重构
    public static TopoEnum verifyRelations(GeoNode n1, GeoNode n2) {
        Geometry g1 = n1.getGeometry();
        Geometry g2 = n2.getGeometry();
        if (!g1.getEnvelopeInternal().intersects(g2.getEnvelopeInternal())) {
            return TopoEnum.SEPARATED;
        }
        final int dimension1 = g1.getDimension();
        final int dimension2 = g2.getDimension();
        final IntersectionMatrix im = g1.relate(g2);
        if (im.isContains()) {
            return TopoEnum.CONTAINS;
        }
        if (im.isCoveredBy()) {
            return  TopoEnum.COVEREDBY;
        }
        if (im.isCovers()) {
            return  TopoEnum.COVERS;

        }
        if (im.isCrosses(dimension1, dimension2)) {
            return  TopoEnum.CROSSES;

        }
        if (im.isEquals(dimension1, dimension2)) {
            return  TopoEnum.EQUALS;

        }
        if (im.isOverlaps(dimension1, dimension2)) {
            return  TopoEnum.OVERLAPS;

        }
        if (im.isTouches(dimension1, dimension2)) {
            return  TopoEnum.TOUCHES;

        }
        if (im.isWithin()) {
            return  TopoEnum.WITHIN;
        }
        if (im.isIntersects()) {
            return  TopoEnum.INTERSECTS;
        }
        return TopoEnum.SEPARATED;
    }

    private boolean isLPorPP(Geometry n1, Geometry n2) {
        if (n1.getGeometryType().equals("Point") && (n2.getGeometryType().equals("Point") || n2.getGeometryType().equals("LineString") )){
            return true;
        }
        if (n1.getGeometryType().equals("LineString") && n2.getGeometryType().equals("Point")){
            return true;
        }
        return false;
    }

    public boolean verifyRelations(int geomId1, int geomId2, Geometry sourceGeom, Geometry targetGeom) {
        try {
            final int dimension1 = sourceGeom.getDimension();
            final int dimension2 = targetGeom.getDimension();
            verifiedPairs++;
            final IntersectionMatrix im = sourceGeom.relate(targetGeom);
            if (isLPorPP(sourceGeom, targetGeom)) {
                if (GeoSpatialRelation.sgrDis >= sourceGeom.distance(targetGeom)){
                    addTouches(geomId1, geomId2);
                    return true;
                }
                return false;
            }
            boolean related = false;
            if (im.isContains()) {
                related = true;
                detectedLinks++;
                addContains(geomId1, geomId2);
            }
            if (im.isCoveredBy()) {
                related = true;
                detectedLinks++;
                addCoveredBy(geomId1, geomId2);
            }
            if (im.isCovers()) {
                related = true;
                detectedLinks++;
                addCovers(geomId1, geomId2);
            }
            if (im.isCrosses(dimension1, dimension2)) {
                related = true;
                detectedLinks++;
                addCrosses(geomId1, geomId2);
            }
            if (im.isEquals(dimension1, dimension2)) {
                related = true;
                detectedLinks++;
                addEquals(geomId1, geomId2);
            }
            if (im.isIntersects()) {
                related = true;
                detectedLinks++;
                addIntersects(geomId1, geomId2);
            }
            if (im.isOverlaps(dimension1, dimension2)) {
                related = true;
                detectedLinks++;
                addOverlaps(geomId1, geomId2);
            }
            if (im.isTouches(dimension1, dimension2)) {
                related = true;
                detectedLinks++;
                addTouches(geomId1, geomId2);
            }
            if (im.isWithin()) {
                related = true;
                detectedLinks++;
                addWithin(geomId1, geomId2);
            }

            if (related) {
                interlinkedGeometries++;
            }

//            if (verifiedPairs == 5000000 || verifiedPairs == 10000000) {
//                System.out.println("\nTime\t:\t" + System.currentTimeMillis());
//                System.out.println(pgr + "\t" + interlinkedGeometries + "\t" + verifiedPairs);
//                System.out.println(pgr / qualifyingPairs / verifiedPairs);
//            }
            return related;
        } catch (Exception ex) {
            ex.printStackTrace();
            exceptions++;
            return false;
        }
    }
}
