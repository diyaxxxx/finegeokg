package com.ustb.GIAnt;

import com.ustb.model.LightIndex;
import com.ustb.util.CsvReader;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GIAnt extends AbstractAlgorithm {

    public GIAnt(int qPairs, String delimiter, String sourceFilePath, String targetFilePath) throws IOException {
        super(qPairs, delimiter, sourceFilePath, targetFilePath);
    }

    @Override
    public void applyProcessing() {
        long time1 = System.currentTimeMillis();
        // 遍历求平均大小
        super.applyProcessing();
        // 建索引
        final LightIndex spatialIndex = indexSource();

        long time2 = System.currentTimeMillis();
        try {
            matchTargetData(spatialIndex);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        long time3 = System.currentTimeMillis();
        indexingTime = time2 - time1;
        verificationTime = time3 - time2;
    }

    private void addToIndex(int geometryId, Envelope envelope, LightIndex spatialIndex) {
        int maxX = (int) Math.ceil(envelope.getMaxX() / thetaX);
        int maxY = (int) Math.ceil(envelope.getMaxY() / thetaY);
        int minX = (int) Math.floor(envelope.getMinX() / thetaX);
        int minY = (int) Math.floor(envelope.getMinY() / thetaY);
//        System.out.print(envelope.getMaxX() + "-" + envelope.getMinX());
//        System.out.println(" " + envelope.getMaxY() + "-" + envelope.getMinY());
//        System.out.print("(x-min, x-max)" + " -- (" + minX + ", " + maxX + ")");
//        System.out.println("   (y-min, y-max)" + " -- (" + minY + ", " + maxY + ")");
        if (maxX - minX > 50 || maxY - minY > 50) {
            return;
        }
        for (int latIndex = minX; latIndex <= maxX; latIndex++) {
            for (int longIndex = minY; longIndex <= maxY; longIndex++) {
                spatialIndex.add(latIndex, longIndex, geometryId);
            }
        }
    }

    private LightIndex indexSource() {
        final LightIndex spatialIndex = new LightIndex();
        for (int i = 0; i < datasetDelimiter; i++) {
            addToIndex(i, sourceData[i].getEnvelopeInternal(), spatialIndex);
        }
        return spatialIndex;
    }

    private void matchTargetData(LightIndex spatialIndex) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(targetFilePath));
        String line = reader.readLine();
        int counter = 0;
        int geoCollections = 0;
        while (line != null) {
            String[] tokens = line.split(delimiter);
            Geometry geometry = null;
            try {
                if (2 <= tokens.length) {
                    geometry = CsvReader.WKT_READER.read(tokens[1].trim());
                    if (geometry == null) {
                        line = reader.readLine();
                        continue;
                    }
                    counter++;
                    final TIntSet candidateMatches = new TIntHashSet();
                    final Envelope envelope = geometry.getEnvelopeInternal();

                    int maxX = (int) Math.ceil(envelope.getMaxX() / thetaX);
                    int maxY = (int) Math.ceil(envelope.getMaxY() / thetaY);
                    int minX = (int) Math.floor(envelope.getMinX() / thetaX);
                    int minY = (int) Math.floor(envelope.getMinY() / thetaY);
                    for (int latIndex = minX; latIndex <= maxX; latIndex++) {
                        for (int longIndex = minY; longIndex <= maxY; longIndex++) {
                            final TIntList partialCandidates = spatialIndex.getSquare(latIndex, longIndex);
                            if (partialCandidates != null) {
                                candidateMatches.addAll(partialCandidates);
                            }
                        }
                    }

                    final TIntIterator intIterator = candidateMatches.iterator();
                    while (intIterator.hasNext()) {
                        int candidateMatchId = intIterator.next();
                        if (sourceData[candidateMatchId].getEnvelopeInternal().intersects(geometry.getEnvelopeInternal())) {
                            relations.verifyRelations(candidateMatchId, counter, sourceData[candidateMatchId], geometry);
                        }
                    }
                }
            } catch (Exception ex) {
                continue;
            }

            line = reader.readLine();
        }
        reader.close();
        // System.out.println("Target geometry collections\t:\t" + geoCollections);
    }

    /**
     * 平均宽高
     */
    @Override
    protected void setThetas() {
        thetaX = 0;
        thetaY = 0;
        for (Geometry sEntity : sourceData) {
            final Envelope en = sEntity.getEnvelopeInternal();
            thetaX += en.getWidth();
            thetaY += en.getHeight();
        }
        thetaX /= sourceData.length;
        thetaY /= sourceData.length;
        System.out.print("平均经纬度： ");
        System.out.println(thetaX + "\t" + thetaY);
    }
}
