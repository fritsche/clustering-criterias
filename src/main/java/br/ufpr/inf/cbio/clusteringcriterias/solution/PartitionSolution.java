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
package br.ufpr.inf.cbio.clusteringcriterias.solution;

import br.ufpr.inf.cbio.clusteringcriterias.dataset.DataPoint;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class PartitionSolution extends AbstractGenericSolution<Integer, IntegerProblem> implements IntegerSolution {

    public PartitionSolution(IntegerProblem problem, InputStream file, Dataset dataset) {
        super(problem);
        initializeIntegerVariables(file, dataset);
        initializeObjectiveValues();
    }

    public PartitionSolution(PartitionSolution solution) {
        super(solution.problem);

        for (int i = 0; i < problem.getNumberOfVariables(); i++) {
            setVariableValue(i, solution.getVariableValue(i));
        }

        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            setObjective(i, solution.getObjective(i));
        }

        attributes = new HashMap<>(solution.attributes);
    }

    @Override
    public Integer getUpperBound(int index) {
        return problem.getUpperBound(index);
    }

    @Override
    public Integer getLowerBound(int index) {
        return problem.getLowerBound(index);
    }

    @Override
    public PartitionSolution copy() {
        return new PartitionSolution(this);
    }

    @Override
    public Map<Object, Object> getAttributes() {
        return null;
    }

    @Override
    public String getVariableValueString(int index) {
        return getVariableValue(index).toString();
    }

    private void initializeIntegerVariables(InputStream file, Dataset dataset) {

        TsvParserSettings settings = new TsvParserSettings();
        TsvParser parser = new TsvParser(settings);
        List<String[]> rows = parser.parseAll(file);
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        List<DataPoint> datapoints = dataset.getDataPoints();
        for (String[] row : rows) {
            int i;
            for (i=0; i< datapoints.size(); i++) {
                if (datapoints.get(i).getId().compareTo(row[0]) == 0) {
                    break;
                }
            }
            int value = Integer.parseInt(row[1]);
            if (value > max) {
                max = value;
            }
            if (value < min) {
                min = value;
            }
            setVariableValue(i, value);
        }
        if (min == 1) {
            max--;
            for (int i = 0; i < getNumberOfVariables() - 1; i++) {
                setVariableValue(i, getVariableValue(i) - 1);
            }
        }
        setVariableValue(getNumberOfVariables() - 1, max + 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PartitionSolution other = (PartitionSolution) obj;
        return other.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
