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

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.ufpr.inf.cbio.clusteringcriterias.runner.Runner;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.algorithm.multiobjective.wasfga.util.WeightVectors;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.fileoutput.FileOutputContext;
import org.uma.jmetal.util.point.util.distance.PointDistance;
import smile.validation.AdjustedRandIndex;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class Utils {

    public static DoubleMatrix2D computeDistanceMatrix(Dataset dataset, PointDistance distance) {
        int n = dataset.getDataPoints().size();
//		System.out.println(n);
//		System.out.println(Runtime.getRuntime().maxMemory());
        DoubleMatrix2D distances = new DenseDoubleMatrix2D(n, n);
//		double[][] distances = new double[n][n]; //implementação anterior

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
//				distances[j][i] = distances[i][j] = distance.compute(dataset.getPoint(i), dataset.getPoint(j));
                distances.set(i, j, distance.compute(dataset.getPoint(i), dataset.getPoint(j)));
                distances.set(j, i, distances.get(i, j));
            }
        }

        return distances;
    }

    public static List<List<Integer>> computeNeighborhood(DoubleMatrix2D distances) {
        int k = 10; // https://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=8004483
        return computeNeighborhood(distances, k);
    }

    public static List<List<Integer>> computeNeighborhood(DoubleMatrix2D distances, int k) {
        int n = distances.rows();
        List<List<Integer>> neighborhood = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            List<Double> di = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                di.add(distances.getQuick(i, j));
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
        List<PartitionSolution> unique = new ArrayList<>();
        for (PartitionSolution s : population) {
            Pair<Double, Double> e = Pair.of(s.getObjective(0), s.getObjective(1));
            if (!list.contains(e)) {
                unique.add(s);
            }
            list.add(e);
        }
        population.clear();
        population.addAll(unique);

        return population;
    }

    public static void computeAdjustedRand(int[] label, List<PartitionSolution> population, //todo: teste do adjusted Rand
            FileOutputContext context) {

        BufferedWriter bufferedWriter = context.getFileWriter();

        AdjustedRandIndex ari = new AdjustedRandIndex();

        try {
            if (population.size() > 0) {
                for (PartitionSolution s : population) {
                    int[] y = new int[s.getNumberOfVariables() - 1];
                    for (int i = 0; i < s.getNumberOfVariables() - 1; i++) {
                        y[i] = s.getVariableValue(i);
                    }
                    double ar = ari.measure(y, label);
                    bufferedWriter.write(String.valueOf(ar));
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.close();
        } catch (IOException e) {
            throw new JMetalException("Error writing data ", e);
        }
    }

    public static void generateWeightVector(Integer div_numb) throws FileNotFoundException {

        double[][] weights = WeightVectors.initializeUniformlyInTwoDimensions(1, div_numb);

//		weights = WeightVectors.invert(weights,true);
        File file = new File(Runner.class.getClassLoader().getResource("weight_vectors/").getFile() + "/a.sld");

        System.out.println("Saving weight vetor at: " + file.getAbsolutePath());

        try (PrintWriter writer = new PrintWriter(file.getAbsolutePath())) {
            writer.write("# " + div_numb + " 2\n");
            
            for (int i = 0; i < div_numb; i++) {
                for (int j = 0; j < 2; j++) {
                    writer.write(weights[i][j] + " ");
                }
                writer.write("\n");
            }

            writer.flush();
        }

    }

}
