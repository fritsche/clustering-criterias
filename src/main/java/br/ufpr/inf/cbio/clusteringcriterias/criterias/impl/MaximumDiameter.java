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
package br.ufpr.inf.cbio.clusteringcriterias.criterias.impl;

import br.ufpr.inf.cbio.clusteringcriterias.criterias.ObjectiveFunction;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.DataPoint;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.point.util.distance.EuclideanDistance;
import org.uma.jmetal.util.point.util.distance.PointDistance;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class MaximumDiameter implements ObjectiveFunction<IntegerSolution> {

    List<List<Double>> distances;

    public MaximumDiameter(Dataset dataset) {
        this(dataset, new EuclideanDistance());
    }

    public MaximumDiameter(Dataset dataset, PointDistance distance) {
        distances = initDistances(dataset, distance);
    }

    @Override
    public double evaluate(IntegerSolution s) {
        int size = s.getNumberOfVariables() - 1;
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // i and j belong to the same cluster?
                if (s.getVariableValue(i).equals(s.getVariableValue(j))) {
                    if (max < distances.get(i).get(j)) {
                        max = distances.get(i).get(j);
                    }
                }
            }
        }
        return max;
    }

    private List<List<Double>> initDistances(Dataset dataset, PointDistance distance) {
        List<List<Double>> distanceMatrix = new ArrayList<>();
        List<DataPoint> datapoints = dataset.getDataPoints();
        for (int i = 0; i < datapoints.size(); i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < datapoints.size(); j++) {
                row.add(distance.compute(datapoints.get(i).getPoint(), datapoints.get(j).getPoint()));
            }
            distanceMatrix.add(row);
        }
        return distanceMatrix;
    }
}
