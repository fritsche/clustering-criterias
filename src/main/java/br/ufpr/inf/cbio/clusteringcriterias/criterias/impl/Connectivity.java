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
import java.util.List;
import org.uma.jmetal.solution.IntegerSolution;

/**
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 *
 * @cite Handl, J., & Knowles, J. (2012). Clustering criteria in multiobjective
 * data clustering. Lecture Notes in Computer Science (Including Subseries
 * Lecture Notes in Artificial Intelligence and Lecture Notes in
 * Bioinformatics), 7492 LNCS(PART 2), 32–41.
 * https://doi.org/10.1007/978-3-642-32964-7_4
 */
public class Connectivity implements ObjectiveFunction<IntegerSolution> {

    private final List<List<Integer>> neighborhood;

    public Connectivity(List<List<Integer>> neighborhood) {
        this.neighborhood = neighborhood;
    }

    /**
     * Connectivity, assesses to what extent data points that are close
     * neighbors are found in the same cluster. It is given as the ratio of
     * neighbors in the same cluster, accumulated for each object, for each
     * cluster.
     *
     * @param s
     * @return
     */
    @Override
    public double evaluate(IntegerSolution s) {
        double connectivity = 0.0;
        for (int i = 0; i < s.getNumberOfVariables() - 1; i++) {
            int c = s.getVariableValue(i);
            double partial = 0.0;
            List<Integer> neighbors = neighborhood.get(i);
            for (int l = 0; l < neighbors.size(); l++) {
                int neighbor = neighbors.get(l);
                if (c != s.getVariableValue(neighbor)) {
//                    System.out.println(i);
//                    System.out.println(neighbor);
//                    System.out.println(c);
//                    System.out.println(s.getVariableValue(neighbor));
                    partial += 1.0 / (double) (l + 1);
                }
            }
            connectivity += partial;
//            System.out.println(connectivity);
        }
//        System.out.println(connectivity);
        return connectivity;
    }

}
