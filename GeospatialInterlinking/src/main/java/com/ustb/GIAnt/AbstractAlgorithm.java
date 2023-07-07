package com.ustb.GIAnt;

import com.ustb.model.RelatedGeometries;
import com.ustb.util.CsvReader;
import org.locationtech.jts.geom.Geometry;

import java.io.IOException;

public abstract class AbstractAlgorithm {

    protected int datasetDelimiter;

    protected double thetaX;
    protected double thetaY;

    protected long indexingTime;
    protected long verificationTime;

    protected Geometry[] sourceData;
    protected RelatedGeometries relations;
    protected final String delimiter;
    protected String targetFilePath;

    public AbstractAlgorithm(int qPairs, String delimiter, String sourceFilePath, String targetFilePath) throws IOException {
        sourceData = CsvReader.readAllEntities(delimiter, sourceFilePath);

        this.delimiter = delimiter;
        this.targetFilePath = targetFilePath;

        relations = new RelatedGeometries(qPairs);
        datasetDelimiter = sourceData.length;
    }

    public void applyProcessing() {
        setThetas();
    }

    public void printResults() {
        System.out.println("建立索引时间\t:\t" + indexingTime + "ms");
        System.out.println("验证时间\t:\t" + verificationTime  + "ms");
        relations.print();
    }
    protected abstract void setThetas();
}
