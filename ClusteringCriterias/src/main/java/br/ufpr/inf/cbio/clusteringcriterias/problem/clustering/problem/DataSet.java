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
package br.ufpr.inf.cbio.clusteringcriterias.problem.clustering.problem;

import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.util.point.Point;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class DataSet {

    private List<DataPoint> dataPoints;

    public DataSet() {
        dataPoints = new ArrayList<>();
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

}
