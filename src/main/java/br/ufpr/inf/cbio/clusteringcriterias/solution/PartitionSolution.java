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

import br.ufpr.inf.cbio.clusteringcriterias.problem.DataPoint;
import br.ufpr.inf.cbio.clusteringcriterias.problem.DataPointComparator;
import br.ufpr.inf.cbio.clusteringcriterias.problem.DataSet;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class PartitionSolution extends AbstractGenericSolution<Integer, IntegerProblem> implements IntegerSolution {

    public PartitionSolution(IntegerProblem problem, File file, DataSet dataset) {
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
    public String getVariableValueString(int index) {
        return getVariableValue(index).toString();
    }

    private void initializeIntegerVariables(File file, DataSet dataset) {

        /**
         * @TODO it it not possible to assume that the smaller cluster id is 0 or 1.
         * There is files where it is 0 and there is files where it is 1.
         * 1. Read the file, fill the variables, compute the smallest and highest id.
         * 2. If the smallest value is 1, discount 1 from each variable. To ensure that the smallest id is 0.
         * 3. Save the k on the first position of the array of variables.
         * 
         */
        
        TsvParserSettings settings = new TsvParserSettings();
        TsvParser parser = new TsvParser(settings);
        List<String[]> rows = parser.parseAll(file);
        for (String[] row : rows) {
            int i = Collections.binarySearch(dataset.getDataPoints(), new DataPoint(row[0], null), new DataPointComparator());
            int value = Integer.parseInt(row[1]) - 1;
            setVariableValue(i, value);
        }
    }
}
