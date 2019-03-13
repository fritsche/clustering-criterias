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

import br.ufpr.inf.cbio.clusteringcriterias.operator.util.GraphCSR;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import com.sun.jna.Library;
import com.sun.jna.Native;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class HBGFCrossover implements CrossoverOperator<PartitionSolution> {

    public interface Partition extends Library {

        int partition(int nvtxs, int[] xadj, int[] adjncy, int nparts, int[] part);
    }
    private Partition partition;
    private int numberOfGeneratedChildren;
    private final int numberOfRequiredParents;
    private double crossoverProbability;

    public HBGFCrossover() {
        this(1.0, 1);
    }

    public HBGFCrossover(int numberOfGeneratedChildren) {
        this(1.0, numberOfGeneratedChildren);
    }

    public HBGFCrossover(double crossoverProbability) {
        this(crossoverProbability, 1);
    }

    public HBGFCrossover(double crossoverProbability, int numberOfGeneratedChildren) {
        this.crossoverProbability = crossoverProbability;
        this.numberOfGeneratedChildren = numberOfGeneratedChildren;
        this.numberOfRequiredParents = 2;
    }

    public Partition getPartition() {
        if (partition == null) {
            // get lib folder from resource
            File file = new File(this.getClass().getClassLoader().getResource("lib").getFile());
            // set jna.library.path to the path of lib
            System.setProperty("jna.library.path", file.getAbsolutePath());
            // load libpartition.so from lib
            partition = (Partition) Native.load("partition", Partition.class);
        }
        return partition;
    }

    public int partition(int nvtxs, int[] xadj, int[] adjncy, int nparts, int[] part) {
        return getPartition().partition(nvtxs, xadj, adjncy, nparts, part);
    }

    public GraphCSR convertToGraph(PartitionSolution a, PartitionSolution b) {
        int n = a.getNumberOfVariables() - 1; // number of objects
        int ka = a.getVariableValue(n); // max cluster id of parent A
        int kb = b.getVariableValue(n); // max cluster id of parent B
        int nvtxs = n + ka + kb; // number of vertices in the graph.
        List<List<Integer>> adjncyList = new ArrayList<>(nvtxs);
        for (int i = 0; i < n; i++) {
            adjncyList.add(new ArrayList<Integer>(2));
        }
        for (int i = n; i < nvtxs; i++) {
            adjncyList.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < n; i++) {
            List<Integer> adjncy_i = adjncyList.get(i);
            // parent A
            int clu_id = a.getVariableValue(i) + n; // get i-th cluster id
            adjncy_i.add(clu_id); // add to adjacency of i
            adjncyList.get(clu_id).add(i); // add i as adjacency of clu_id
            // parent B
            clu_id = b.getVariableValue(i) + n + ka;
            adjncy_i.add(clu_id);
            adjncyList.get(clu_id).add(i);
        }
        int xadj[] = new int[nvtxs + 1];
        int adjncy[] = new int[n * (ka + kb)];
        int j = 0;
        for (int i = 0; i < adjncyList.size(); i++) {
            xadj[i] = j;
            for (Integer integer : adjncyList.get(i)) {
                adjncy[j++] = integer;
            }
        }
        xadj[nvtxs] = n * (ka + kb);
        return new GraphCSR(nvtxs, xadj, adjncy);
    }

    public PartitionSolution hbgf(PartitionSolution a, PartitionSolution b) {
        GraphCSR gcsr = convertToGraph(a, b);
        int nvtxs = gcsr.getNumberOfVertices(); // number of vertices
        int k = JMetalRandom.getInstance().nextInt(a.getVariableValue(a.getNumberOfVariables() - 1), b.getVariableValue(b.getNumberOfVariables() - 1));
        int part[] = new int[nvtxs];
        getPartition().partition(nvtxs, gcsr.getAdjacencyIndexes(), gcsr.getAdacencies(), k, part);

        PartitionSolution child = (PartitionSolution) a.copy();
        for (int i = 0; i < a.getNumberOfVariables() - 1; i++) {
            child.setVariableValue(i, part[i]);
        }
        child.setVariableValue(child.getNumberOfVariables() - 1, k);
        return child;
    }

    @Override
    public int getNumberOfRequiredParents() {
        return numberOfRequiredParents;
    }

    @Override
    public int getNumberOfGeneratedChildren() {
        return numberOfGeneratedChildren;
    }

    @Override
    public List<PartitionSolution> execute(List<PartitionSolution> source) {
        if (null == source) {
            throw new JMetalException("Null parameter");
        } else if (source.size() != numberOfRequiredParents) {
            throw new JMetalException("There must be " + numberOfRequiredParents + " parents instead of " + source.size());
        }
        return doCrossover(crossoverProbability, source.get(0), source.get(1));
    }

    public List<PartitionSolution> doCrossover(double probability, PartitionSolution parent1, PartitionSolution parent2) {
        List<PartitionSolution> offspring = new ArrayList<>();
        while (offspring.size() < numberOfGeneratedChildren) {
            offspring.add(hbgf(parent1, parent2));
        }
        return offspring;
    }

}
