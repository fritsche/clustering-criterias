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

import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class ClusteringCrossover implements CrossoverOperator<PartitionSolution> {

    @Override
    public int getNumberOfRequiredParents() {
        return 2;
    }

    @Override
    public int getNumberOfGeneratedChildren() {
        return 1;
    }

    @Override
    public List<PartitionSolution> execute(List<PartitionSolution> source) {
        PartitionSolution father = convert(source.get(0));
        PartitionSolution mother = convert(source.get(1));
        PartitionSolution child = father.copy();
        JMetalRandom random = JMetalRandom.getInstance();
        for (int i = 0; i < child.getNumberOfVariables() - 1; i++) {
            if (random.nextDouble() < 0.5) {
                child.setVariableValue(i, father.getVariableValue(i));
            } else {
                child.setVariableValue(i, mother.getVariableValue(i));
            }
        }
        List<PartitionSolution> offspring = new ArrayList<>(1);
        offspring.add(child);
        return offspring;
    }

    private PartitionSolution convert(PartitionSolution parent) {
        int size = parent.getNumberOfVariables() - 1;
        int k = size;
        for (int i = 0; i < size; i++) {
            int vari = parent.getVariableValue(i);
            if (vari < size) {
                for (int j = i; j < size; j++) {
                    int varj = parent.getVariableValue(j);
                    if (varj < size) {
                        parent.setVariableValue(j, k);
                    }
                }
            }
            k++;
        }
        for (int i = 0; i < size; i++) {
            parent.setVariableValue(i, parent.getVariableValue(i) - size);
        }
        return parent;
    }

}
