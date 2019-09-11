/*
 * Copyright (C) 2019 Gian Fritsche <gmfritsche at inf.ufpr.br>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.ufpr.inf.cbio.clusteringcriterias.dataset;

import br.ufpr.inf.cbio.clusteringcriterias.problem.Utils;
import br.ufpr.inf.cbio.clusteringcriterias.runner.Runner;
import cern.colt.matrix.DoubleMatrix2D;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jep.NDArray;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;
import org.uma.jmetal.util.point.util.distance.EuclideanDistance;
import org.uma.jmetal.util.point.util.distance.PointDistance;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class Dataset {

    private List<DataPoint> dataPoints;
    private final String initialPartitionsPath;
    private int[] truePartition;
    private String dataSetPath;
    private double maxDist = Double.NEGATIVE_INFINITY;
    private DoubleMatrix2D distanceMatrix = null;
    private NDArray<double[]> featureArray = null;

    public Dataset(String dataSetPath, String initialPartitionsPath, String truePartitionPath) {
        this(dataSetPath, initialPartitionsPath);
        this.truePartition = parseTruePartition(truePartitionPath);
    }

    public Dataset(String dataSetPath, String initialPartitionsPath) {
        this.initialPartitionsPath = initialPartitionsPath;
        this.dataSetPath = dataSetPath;
        this.dataPoints = parseFile(new File(getClass().getClassLoader().getResource(dataSetPath).getFile()));
    }

    public static List<DataPoint> parseFile(File file) {
        List<DataPoint> dps = new ArrayList<>();
        TsvParserSettings settings = new TsvParserSettings();
        settings.setMaxColumns(3600);
        TsvParser parser = new TsvParser(settings);
        List<String[]> rows = parser.parseAll(file);
        rows.remove(0);
        for (String[] row : rows) {
            String id = row[0];
            double[] point = new double[row.length - 1];
            for (int i = 1; i < row.length; i++) {
                point[i - 1] = Double.parseDouble(row[i]);
            }
            Point p = new ArrayPoint(point);
            DataPoint data = new DataPoint(id, p);
            dps.add(data);
        }
        return dps;
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public void addDataPoint(String id, Point p) {
        dataPoints.add(new DataPoint(id, p));
    }

    public Point getPoint(int i) {
        return dataPoints.get(i).getPoint();
    }

    public int getDimension() {
        return dataPoints.get(0).getPoint().getDimension();
    }

    @Override
    public String toString() {
        String string = "";
        for (DataPoint point : dataPoints) {
            string += point.toString() + "\n";
        }
        return string;
    }

    public List<File> getInitialPartitionFiles() {
        List<File> files = new ArrayList<>();
        List<String> paths = new ArrayList<>();
        try {
            paths = IOUtils.readLines(Utils.class.getClassLoader().getResourceAsStream(initialPartitionsPath), Charsets.UTF_8);
        } catch (IOException ex) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, "Error reading [" + initialPartitionsPath + "] folder.", ex);
        }
        for (String file : paths) {
            files.add(new File(Utils.class.getClassLoader().getResource(initialPartitionsPath + File.separator + file).getFile()));
        }
        return files;
    }

    public static int[] parseTruePartition(String truePartitionPath) {

        File file = new File(Dataset.class.getClassLoader().getResource(truePartitionPath).getFile());
        TsvParserSettings settings = new TsvParserSettings();
        TsvParser parser = new TsvParser(settings);
        List<String[]> rows = parser.parseAll(file);
//        Collections.sort(rows, Comparator.comparing(o -> o[0])); //todo: verificar se TPs estão na ordem do dataset

        int[] label = new int[rows.size()];
        int index = 0;
        for (String[] row : rows) {
            label[index] = Integer.parseInt(row[1]);
            index++;
        }
        return label;
    }

    public int[] getTruePartition() {
        return truePartition;
    }

    public String getDatasetPath() {
        return dataSetPath;
    }

    public double getMaximumDistanceInDataset() {
        if (maxDist == Double.NEGATIVE_INFINITY) {
            PointDistance distance = new EuclideanDistance();
            List<DataPoint> datapoints = this.getDataPoints();
            for (int i = 0; i < datapoints.size(); i++) {
                for (int j = 0; j < datapoints.size(); j++) {
                    double dist = distance.compute(datapoints.get(i).getPoint(), datapoints.get(j).getPoint());
                    if (maxDist < dist) {
                        maxDist = dist;
                    }
                }
            }
        }
        return maxDist;
    }

    public DoubleMatrix2D getDistanceMatrix() {
        if (distanceMatrix == null) {
            distanceMatrix = Utils.computeDistanceMatrix(this, new EuclideanDistance());
        }
        return distanceMatrix;
    }

    public NDArray<double[]> getFeatureArray() {
        if (featureArray == null) {
            double[] array = new double[dataPoints.size() * dataPoints.get(0).getPoint().getDimension()];
            int count = 0;
            for (DataPoint dp : dataPoints) {
                Point p = dp.getPoint();
                for (int i = 0; i < p.getDimension(); i++) {
                    array[count++] = p.getValue(i);
                }
            }
            featureArray = new NDArray<>(array, dataPoints.size(), dataPoints.get(0).getPoint().getDimension());
        }
        return featureArray;
    }

}
