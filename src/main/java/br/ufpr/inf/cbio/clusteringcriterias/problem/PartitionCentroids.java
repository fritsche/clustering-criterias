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
import java.util.HashMap;
import java.util.Map;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;
import org.uma.jmetal.util.solutionattribute.impl.GenericSolutionAttribute;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class PartitionCentroids
        extends GenericSolutionAttribute<IntegerSolution, Map<Integer, Point>> {

    public PartitionCentroids() {
    }

    public void computeCentroids(IntegerSolution s, Dataset dataset) {

        Map<Integer, Point> sum = new HashMap();
        Map<Integer, Integer> count = new HashMap();

        for (int i = 0; i < s.getNumberOfVariables() - 1; i++) {
            Integer c = s.getVariableValue(i);
            Point o = new ArrayPoint(dataset.getPoint(i));
            if (sum.containsKey(c)) {
                Point p = sum.get(c);
                count.put(c, count.get(c) + 1);
                for (int j = 0; j < p.getDimension(); j++) {
                    p.setValue(j, p.getValue(j) + o.getValue(j));
                }
            } else {
                sum.put(c, o);
                count.put(c, 1);
            }
        }

        Map<Integer, Point> centroids = new HashMap();

        for (Map.Entry<Integer, Point> entry : sum.entrySet()) {
            Integer key = entry.getKey();
            Point p = entry.getValue();
            for (int j = 0; j < p.getDimension(); j++) {
                p.setValue(j, p.getValue(j) / (double) count.get(key));
            }
            centroids.put(key, p);
        }

        this.setAttribute(s, centroids);
    }
}
