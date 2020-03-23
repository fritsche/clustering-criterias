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
package br.ufpr.inf.cbio.clusteringcriterias.operator;

import br.ufpr.inf.cbio.clusteringcriterias.criterias.JepUtils;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.Connectivity;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.DaviesBouldin;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.OverallDeviation;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import br.ufpr.inf.cbio.clusteringcriterias.operator.util.GraphCSR;
import br.ufpr.inf.cbio.clusteringcriterias.problem.PartitionCentroids;
import br.ufpr.inf.cbio.clusteringcriterias.problem.Utils;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import cern.colt.matrix.DoubleMatrix2D;
import com.sun.jna.Library;
import com.sun.jna.Native;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jep.Interpreter;
import jep.JepException;
import jep.NDArray;
import jep.SharedInterpreter;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.point.util.distance.EuclideanDistance;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * This class implements a CSPA cluster ensemble for crossover
 *
 * @author Adriano F. Kultzak <adriano_fk@hotmail.com>
 */
public class CSPACrossover implements CrossoverOperator<PartitionSolution> {

    private final int numberOfRequiredParents;
    private final NDArray<double[]> featureArray;
    private final Dataset dataset;

    public CSPACrossover(Dataset dataset) throws JepException {
        JepUtils.initializePython3Interpreter();
        this.featureArray = null;
        this.numberOfRequiredParents = 2;
        this.dataset = dataset;
    }


    public PartitionSolution cspa(PartitionSolution a, PartitionSolution b) {

        int k;
        int[] a_array, b_array;
        a_array = new int[a.getNumberOfVariables() - 1];
        b_array = new int[b.getNumberOfVariables() - 1];
        for (int i = 0; i < a.getNumberOfVariables() - 1; i++) {
            a_array[i] = a.getVariableValue(i);
            b_array[i] = b.getVariableValue(i);
        }
        NDArray<int[]> a_labels = new NDArray<>(a_array, a_array.length);
        NDArray<int[]> b_labels = new NDArray<>(b_array, b_array.length);

        int min = Math.min(a.getVariableValue(a.getNumberOfVariables() - 1), b.getVariableValue(b.getNumberOfVariables() - 1));
        int max = Math.max(a.getVariableValue(a.getNumberOfVariables() - 1), b.getVariableValue(b.getNumberOfVariables() - 1));

        k = JMetalRandom.getInstance().nextInt(min, max);

        ArrayList al = null;
        try (Interpreter interp = new SharedInterpreter()) {
            interp.exec("import numpy as np");
            interp.exec("import CSPA");
            interp.set("a_labels", a_labels);
            interp.set("b_labels", b_labels);
            interp.set("k",k);
            interp.exec("cluster_runs = np.vstack((a_labels,b_labels))");
            interp.exec("consensus_clustering_labels = CSPA.CSPA(cluster_runs,k)");
            al = (ArrayList) interp.getValue("consensus_clustering_labels");
        } catch (JepException ex) {
            Logger.getLogger(DaviesBouldin.class.getName()).log(Level.SEVERE, "Error to execute python script!", ex);
        }

        PartitionSolution child = a.copy();
        for (int i = 0; i < a.getNumberOfVariables() - 1; i++) {
            child.setVariableValue(i, Math.toIntExact((Long) al.get(i)));
        }
        PartitionSolution copy = child.copy();
        int v, n = copy.getNumberOfVariables() - 1, aux = 0;
        for (int i = 0; i < n; i++) {
            v = copy.getVariableValue(i);
            if (v >= 0) {
                aux++;
                copy.setVariableValue(i, -1);
                for (int j = i; j < n; j++) {
                    if (copy.getVariableValue(j) == v) {
                        copy.setVariableValue(j, -1);
                    }
                }
            }
        }
        child.setVariableValue(child.getNumberOfVariables() - 1, aux);

//        System.out.printf("PAI 1 - Deviation: "+a.getObjective(0)+" Connectivity: "+a.getObjective(1)+"\n");
//        System.out.printf("PAI 2 - Deviation: "+b.getObjective(0)+" Connectivity: "+b.getObjective(1)+"\n");
//        System.out.println(a.getVariables());
//        System.out.println(b.getVariables());
//        System.out.println(al);

//        //conta o novo número de clusters
//        ArrayList<Integer> diffNum = new ArrayList<>();
//        for(int i=0; i< child.getNumberOfVariables()-1; i++){
//            if(!diffNum.contains(child.getVariableValue(i))){
//                diffNum.add(child.getVariableValue(i));
//            }
//        }
//        child.setVariableValue(child.getNumberOfVariables()-1,diffNum.size());
//
//        //corrige os gaps caso existam na solução mutada
//        Collections.sort(diffNum);
//        for (int i = 0; i < diffNum.size(); i++){
//            if(diffNum.get(i) != diffNum.indexOf(diffNum.get(i))){
//                for(int i1 = 0; i1 < child.getNumberOfVariables()-1; i1++){
//                    child.setVariableValue(i1,diffNum.indexOf(child.getVariableValue(i1)));
//                }
//                break;
//            }
//        }
//
////        System.out.println(child);
//        PartitionCentroids partitionCentroids = new PartitionCentroids();
//        partitionCentroids.computeCentroids(child, dataset);
//
//        DoubleMatrix2D distanceMatrix = Utils.computeDistanceMatrix(dataset, new EuclideanDistance());
//
//        List<List<Integer>> neighborhood = Utils.computeNeighborhood(distanceMatrix);
//
//        Connectivity connectivity = new Connectivity(neighborhood);
//
//        OverallDeviation instance = new OverallDeviation(dataset, new EuclideanDistance());
//
//        double dev = instance.evaluate(child);
//        double con = connectivity.evaluate(child);
//        System.out.printf("FILHO - Deviation: "+dev+" Connectivity: "+con+"\n");

        return child;
    }


    @Override
    public int getNumberOfRequiredParents() {
        return numberOfRequiredParents;
    }

    @Override
    public int getNumberOfGeneratedChildren() {
        return 1;
    }

    @Override
    public List<PartitionSolution> execute(List<PartitionSolution> source) {
        if (null == source) {
            throw new JMetalException("Null parameter");
        } else if (source.size() != numberOfRequiredParents) {
            throw new JMetalException("There must be " + numberOfRequiredParents + " parents instead of " + source.size());
        }
        return doCrossover(source.get(0), source.get(1));
    }

    public List<PartitionSolution> doCrossover(PartitionSolution parent1, PartitionSolution parent2) {
        List<PartitionSolution> offspring = new ArrayList<>();
        offspring.add(cspa(parent1, parent2));
        return offspring;
    }

}
