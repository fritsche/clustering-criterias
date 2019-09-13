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
import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import br.ufpr.inf.cbio.clusteringcriterias.problem.PartitionCentroids;
import java.util.Map;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.util.distance.EuclideanDistance;
import org.uma.jmetal.util.point.util.distance.PointDistance;

/**
 * Cluster Separation. Cluster separation Sep or intercluster distance [Ripon et
 * al. 2006a] is computed as the average distance among the cluster centers.
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class MinimizationSeparation implements ObjectiveFunction<IntegerSolution> {

    private final double maximumDistanceInDataset;
    private final PointDistance distanceMeasure;

    public MinimizationSeparation(Dataset dataset) {
        this.distanceMeasure = new EuclideanDistance();
        maximumDistanceInDataset = dataset.getMaximumDistanceInDataset();
    }

    @Override
    public double evaluate(IntegerSolution s) {
        Map<Integer, Point> centroids = (new PartitionCentroids()).getAttribute(s);
        int k = centroids.size();
        double sum = 0.0;
        for (Map.Entry<Integer, Point> i : centroids.entrySet()) {
            int key_i = i.getKey();
            Point center_i = i.getValue();
            for (Map.Entry<Integer, Point> j : centroids.entrySet()) {
                int key_j = j.getKey();
                Point center_j = j.getValue();
                if (key_i != key_j) {
                    sum += distanceMeasure.compute(center_i, center_j);
                }
            }
        }
        double separation = (2.0 / (k * (k - 1))) * sum;
        // normalize
        separation = separation / (maximumDistanceInDataset * 2.0);
        // invert
        separation = (1.0 - separation);
        return separation;
    }
}
