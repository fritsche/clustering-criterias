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
import br.ufpr.inf.cbio.clusteringcriterias.problem.DataSet;
import br.ufpr.inf.cbio.clusteringcriterias.problem.PartitionCentroids;
import java.util.HashMap;
import java.util.Map;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

/**
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 *
 * @cite Handl, J., & Knowles, J. (2012). Clustering criteria in multiobjective
 * data clustering. Lecture Notes in Computer Science (Including Subseries
 * Lecture Notes in Artificial Intelligence and Lecture Notes in
 * Bioinformatics), 7492 LNCS(PART 2), 32–41.
 * https://doi.org/10.1007/978-3-642-32964-7_4
 */
public class OverallDeviation implements ObjectiveFunction<IntegerSolution> {

    private DataSet dataSet;

    public OverallDeviation(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Overall deviation, measures the compactness of the clusters in the
     * partitioning. It is the sum of the distances of each object to the
     * centroid, accumulated for all clusters.
     *
     * @param s
     * @return
     */
    @Override
    public double evaluate(IntegerSolution s) {

        Map<Integer, Point> centroids = (new PartitionCentroids()).getAttribute(s);

        /**
         * @TODO 1. Compute each centroid Point 2. Compute the distance of each
         * object Point to its corresponding centroid 3. Accumulate the
         * distances
         */
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
