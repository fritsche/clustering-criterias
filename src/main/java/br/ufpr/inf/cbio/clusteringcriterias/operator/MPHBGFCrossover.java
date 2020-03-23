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
import java.util.Collections;
import java.util.List;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class MPHBGFCrossover implements CrossoverOperator<PartitionSolution> {

    public interface Partition extends Library {

        int partition(int nvtxs, int[] xadj, int[] adjncy, int nparts, int[] part);
    }
    private Partition partition;
    private final int numberOfRequiredParents;
    private final int kmax;

    public MPHBGFCrossover(int kmax) {
        this.kmax = kmax;
        this.numberOfRequiredParents = 3;}

    public MPHBGFCrossover() {
        this.numberOfRequiredParents = 3;
        this.kmax = -1;
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

    public GraphCSR convertToGraph(PartitionSolution a, PartitionSolution b, PartitionSolution c) {
        int n = a.getNumberOfVariables() - 1; // number of objects
        int ka = a.getVariableValue(n); // max cluster id of parent A
        int kb = b.getVariableValue(n); // max cluster id of parent B
        int kc = c.getVariableValue(n); // max cluster id of parent B
        int nvtxs = n + ka + kb + kc; // number of vertices in the graph.
        int adjncy[] = new int[n * 3 * 2]; // n objects times 3 parents times 2 (each edge is represented twice, e.g. (a,b) and (b,a)).
        int xadj[] = new int[nvtxs + 1];
        int k = n, v, aj = n * 3, xj = n;

        PartitionSolution x = (PartitionSolution) a.copy(); // copy solutions
        for (int i = 0; i < n; i++) {
            v = x.getVariableValue(i);
            if (v < n) {
                xadj[xj++] = aj;
                for (int j = i; j < n; j++) {
                    if (x.getVariableValue(j) == v) {
                        x.setVariableValue(j, k);
                        xadj[j] = j * 3;
                        adjncy[j * 3] = k;
                        adjncy[aj++] = j;
                    }
                }
                k++;
            }
        }
        PartitionSolution y = (PartitionSolution) b.copy();
        for (int i = 0; i < n; i++) {
            v = y.getVariableValue(i);
            if (v < n) {
                xadj[xj++] = aj;
                for (int j = i; j < n; j++) {
                    if (y.getVariableValue(j) == v) {
                        y.setVariableValue(j, k);
                        adjncy[(j * 3) + 1] = k;
                        adjncy[aj++] = j;
                    }
                }
                k++;
            }
        }
        PartitionSolution z = (PartitionSolution) c.copy();
        for (int i = 0; i < n; i++) {
            v = z.getVariableValue(i);
            if (v < n) {
                xadj[xj++] = aj;
                for (int j = i; j < n; j++) {
                    if (z.getVariableValue(j) == v) {
                        z.setVariableValue(j, k);
                        adjncy[(j * 3) + 2] = k;
                        adjncy[aj++] = j;
                    }
                }
                k++;
            }
        }
        xadj[nvtxs] = n * 6;
        return new GraphCSR(nvtxs, xadj, adjncy);
    }

    public PartitionSolution hbgf(PartitionSolution a, PartitionSolution b, PartitionSolution c) {
        GraphCSR gcsr = convertToGraph(a, b, c);
        int k;
        int nvtxs = gcsr.getNumberOfVertices(); // number of vertices
        int min = Math.min(a.getVariableValue(a.getNumberOfVariables() - 1), b.getVariableValue(b.getNumberOfVariables() - 1));
        int max1 = Math.max(a.getVariableValue(a.getNumberOfVariables() - 1), b.getVariableValue(b.getNumberOfVariables() - 1));
        int max = Math.max(max1, c.getVariableValue(b.getNumberOfVariables() - 1));

        if (kmax == -1){
            k = JMetalRandom.getInstance().nextInt(min, max);
        }
        else {
            k = JMetalRandom.getInstance().nextInt(2, kmax); //teste com numero diferente de min max
        }


        int part[] = new int[nvtxs];

        getPartition().partition(nvtxs, gcsr.getAdjacencyIndexes(), gcsr.getAdacencies(), k, part);

        PartitionSolution child = (PartitionSolution) a.copy();
        for (int i = 0; i < a.getNumberOfVariables() - 1; i++) {
            child.setVariableValue(i, part[i]);
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

        //conta o novo número de clusters
        ArrayList<Integer> diffNum = new ArrayList<>();
        for(int i=0; i< child.getNumberOfVariables()-1; i++){
            if(!diffNum.contains(child.getVariableValue(i))){
                diffNum.add(child.getVariableValue(i));
            }
        }
        child.setVariableValue(child.getNumberOfVariables()-1,diffNum.size());

        //corrige os gaps caso existam na solução mutada
        Collections.sort(diffNum);
        for (int i = 0; i < diffNum.size(); i++){
            if(diffNum.get(i) != diffNum.indexOf(diffNum.get(i))){
                for(int i1 = 0; i1 < child.getNumberOfVariables()-1; i1++){
                    child.setVariableValue(i1,diffNum.indexOf(child.getVariableValue(i1)));
                }
                break;
            }
        }

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
        return doCrossover(source.get(0), source.get(1), source.get(2));
    }

    public List<PartitionSolution> doCrossover(PartitionSolution parent1, PartitionSolution parent2, PartitionSolution parent3) {
        List<PartitionSolution> offspring = new ArrayList<>();
        offspring.add(hbgf(parent1, parent2, parent3));
        return offspring;
    }

}
