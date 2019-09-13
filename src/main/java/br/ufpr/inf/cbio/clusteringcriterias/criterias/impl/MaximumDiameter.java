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
import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import cern.colt.matrix.DoubleMatrix2D;
import org.uma.jmetal.solution.IntegerSolution;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class MaximumDiameter implements ObjectiveFunction<IntegerSolution> {

    private final DoubleMatrix2D distances;

    public MaximumDiameter(Dataset dataset) {
        distances = dataset.getDistanceMatrix();
    }

    @Override
    public double evaluate(IntegerSolution s) {
        int size = s.getNumberOfVariables() - 1;
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // i and j belong to the same cluster?
                if (s.getVariableValue(i).equals(s.getVariableValue(j))) {
                    double d = distances.getQuick(i, j);
                    if (max < d) {
                        max = d;
                    }
                }
            }
        }
        return max;
    }
}
