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
import com.sun.jna.Library;
import com.sun.jna.Native;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class HBGFCrossover {

    public interface Partition extends Library {

        int partition(int nvtxs, int[] xadj, int[] adjncy, int nparts, int[] part);
    }
    private Partition partition;

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

    public GraphCSR convertToGraph(List<Integer> a, List<Integer> b) {
        int n = a.size() - 1; // number of objects
        int ka = a.get(n); // max cluster id of parent A
        int kb = b.get(n); // max cluster id of parent B
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
            int clu_id = a.get(i) + n; // get i-th cluster id
            adjncy_i.add(clu_id); // add to adjacency of i
            adjncyList.get(clu_id).add(i); // add i as adjacency of clu_id
            // parent B
            clu_id = b.get(i) + n + ka;
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

    public List<Integer> hbgf(List<Integer> a, List<Integer> b) {
        GraphCSR gcsr = convertToGraph(a, b);
        int nvtxs = gcsr.getNvtxs(); // number of vertices
        int k = JMetalRandom.getInstance().nextInt(a.get(a.size() - 1), b.get(b.size() - 1));
        int part[] = new int[nvtxs];
        getPartition().partition(nvtxs, gcsr.getXadj(), gcsr.getAdjncy(), k, part);
        List<Integer> consensus = new ArrayList<>(a.size());
        for (int i = 0; consensus.size() < a.size() - 1; i++) {
            consensus.add(part[i]);
        }
        consensus.add(k);
        return consensus;
    }

}
