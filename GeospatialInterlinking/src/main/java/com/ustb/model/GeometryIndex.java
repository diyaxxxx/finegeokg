package com.ustb.model;

import java.io.Serializable;

public class GeometryIndex implements Serializable {

    private static final long serialVersionUID = 13483254243447L;

    private final int datasetLimit;
    private final int noOfEntities;
    private final int totalBlocks;

    private int[][] entityBlocks;

    public GeometryIndex(int datasetLimit, int noOfEntities, Tile[] blocks) {
        if (blocks.length == 0) {
            System.err.println("Geometry index_v1 received an empty block collection as input!");
        }

        totalBlocks = blocks.length;
        this.datasetLimit = datasetLimit;
        this.noOfEntities = noOfEntities;

        indexGeometries(blocks);
    }

    public int[] getEntityBlocks(int entityId) {
        if (noOfEntities <= entityId) {
            return null;
        }

        return entityBlocks[entityId];
    }

    public int getNoOfBlocks() {
        return totalBlocks;
    }

    public int getNoOfCommonBlocks(int sourceId, int targetId) {
        int[] blocks1 = entityBlocks[sourceId];
        int[] blocks2 = entityBlocks[targetId];

        int commonBlocks = 0;
        int noOfBlocks1 = blocks1.length;
        int noOfBlocks2 = blocks2.length;

        int i = 0;
        int j = 0;

        while (i < noOfBlocks1) {
            while (j < noOfBlocks2) {
                if (blocks2[j] < blocks1[i]) {
                    j++;
                } else if (blocks1[i] < blocks2[j]) {
                    break;
                } else { //blocks1[i] == blocks2[j]
                    j++;
                    commonBlocks++;
                }
            }
            i++;
        }
        return commonBlocks;
    }

    public int getNoOfEntityBlocks(int entityId) {
        if (entityBlocks[entityId] == null) {
            return -1;
        }

        return entityBlocks[entityId].length;
    }

    private void indexGeometries(Tile[] tiles) {
        //count valid entities & blocks per entity
        int[] counters = new int[noOfEntities];
        for (Tile tile : tiles) {
            for (int id1 : tile.getSourceGeometries()) {
                counters[id1]++;
            }

            for (int id2 : tile.getTargetGeometries()) {
                int entityId = datasetLimit + id2;
                counters[entityId]++;
            }
        }

        //initialize inverted index_v1
        entityBlocks = new int[noOfEntities][];
        for (int i = 0; i < noOfEntities; i++) {
            entityBlocks[i] = new int[counters[i]];
            counters[i] = 0;
        }

        //build inverted index_v1
        for (int i = 0; i < totalBlocks; i++) {
            for (int id1 : tiles[i].getSourceGeometries()) {
                entityBlocks[id1][counters[id1]] = i;
                counters[id1]++;
            }

            for (int id2 : tiles[i].getTargetGeometries()) {
                int entityId = datasetLimit + id2;
                entityBlocks[entityId][counters[entityId]] = i;
                counters[entityId]++;
            }
        }
    }
}
