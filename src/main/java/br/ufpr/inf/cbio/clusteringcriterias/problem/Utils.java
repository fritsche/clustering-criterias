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

import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.util.point.util.distance.PointDistance;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class Utils {

    public static double[][] computeDistanceMatrix(Dataset dataset, PointDistance distance) {
        int n = dataset.getDataPoints().size();
        double[][] distances = new double[n][n];
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                distances[j][i] = distances[i][j] = distance.compute(dataset.getPoint(i), dataset.getPoint(j));
            }
        }

        return distances;
    }

    public static List<List<Integer>> computeNeighborhood(double[][] distances) {
        int k = 10; // https://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=8004483
        return computeNeighborhood(distances, k);
    }

    public static List<List<Integer>> computeNeighborhood(double[][] distances, int k) {
        int n = distances.length;
        List<List<Integer>> neighborhood = new ArrayList<>(n);
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
                    // int da = (int) (di.get(a) * 1e6);
                    // int db = (int) (di.get(b) * 1e6);
                    // if (da < db) { // to get the same results as MOCLE
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

    public static List<PartitionSolution> removeRepeated(List<PartitionSolution> population) {

        List<Pair<Double, Double>> list = new ArrayList<>();
        List<PartitionSolution> repeated = new ArrayList<>();
        for (PartitionSolution s : population) {
            Pair<Double, Double> e = Pair.of(s.getObjective(0), s.getObjective(1));
            if(list.contains(e)){
                repeated.add(s);
            }
            list.add(e);
        }
        population.removeAll(repeated);

        return population;
    }
}
