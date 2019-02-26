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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.point.util.distance.PointDistance;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class Utils {

    public static double[][] computeDistanceMatrix(DataSet dataSet, PointDistance distance) {
        int n = dataSet.getDataPoints().size();
        double[][] distances = new double[n][n];
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                distances[j][i] = distances[i][j] = distance.compute(dataSet.getPoint(i), dataSet.getPoint(j));
            }
        }

        return distances;
    }

    public static List<List<Integer>> computeNeighborhood(double[][] distances, int k) {
        int n = distances.length;
        List<List<Integer>> neighborhood = new ArrayList<>(n);
        if (k >= n) {
            throw new JMetalException("The number of neighbors k should be smaller than the number of data points n.");
        }
        for (int i = 0; i < n; i++) {
            List<Double> di = new ArrayList<>();
            for (double d : distances[i]) {
                di.add(d);
            }
            neighborhood.add(getNeighbors(di, i, k));
        }
        return neighborhood;
    }

    public static List<Integer> getNeighbors(List<Double> di, int i, int k) {
        int n = di.size();
        di.add(Double.POSITIVE_INFINITY);
        List<Integer> neighbors = new ArrayList<>(Collections.nCopies(k + 1, n));
        for (int j = 0; j < n; j++) {
            if (j != i) {
                neighbors.set(k, j);
                for (int l = k; l > 0; l--) {
                    int a = neighbors.get(l);
                    int b = neighbors.get(l - 1);
                    if (di.get(a) < di.get(b)) {
                        neighbors.set(l, b);
                        neighbors.set(l - 1, a);
                    } else {
                        break;
                    }
                }
            }
        }
        neighbors.remove(k);
        di.remove(n);
        return neighbors;
    }

}
