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
package br.ufpr.inf.cbio.clusteringcriterias.problem;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class DataSet {

    private List<DataPoint> dataPoints;

    public DataSet() {
        dataPoints = new ArrayList<>();
    }

    public DataSet(File file) {
        this();
        TsvParserSettings settings = new TsvParserSettings();
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
            dataPoints.add(data);
        }
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

}
