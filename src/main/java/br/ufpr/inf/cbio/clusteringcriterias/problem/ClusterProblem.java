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

import br.ufpr.inf.cbio.clusteringcriterias.dataset.DataPointComparator;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.ObjectiveFunction;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.DataPoint;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

/**
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class ClusterProblem extends AbstractIntegerProblem {

    private final boolean computeCentroids;
    private final Dataset dataset;
    private final List<ObjectiveFunction> objectiveFunctions;
    private final List<PartitionSolution> initialPopulation;
    private int nextSolution = 0;
    private int populationSize;

    public ClusterProblem(boolean computeCentroids, Dataset dataset,
            List<ObjectiveFunction> objectiveFunctions) {

        this.computeCentroids = computeCentroids;
        this.dataset = dataset;
        this.objectiveFunctions = objectiveFunctions;
        this.setNumberOfVariables(dataset.getDataPoints().size() + 1);
        this.setNumberOfObjectives(objectiveFunctions.size());
        initialPopulation = this.parseInitialPopulation(dataset.getInitialPartitionFiles());
        this.populationSize = initialPopulation.size();
    }

    @Override
    public PartitionSolution createSolution() {
        PartitionSolution solution = initialPopulation.get(nextSolution);
        nextSolution = (nextSolution + 1) % initialPopulation.size();
        return solution;
    }

    @Override
    public void evaluate(IntegerSolution solution) {
        if (computeCentroids) {
            (new PartitionCentroids()).computeCentroids(solution, dataset);
        }
        for (int i = 0; i < objectiveFunctions.size(); i++) {
            solution.setObjective(i, objectiveFunctions.get(i).evaluate(solution));
        }
    }

    private List<PartitionSolution> parseInitialPopulation(List<File> initialPartitions) {
        List<PartitionSolution> population = new ArrayList<>(initialPartitions.size());
        Collections.sort(dataset.getDataPoints(), new DataPointComparator());
        for (File file : initialPartitions) {
            population.add(new PartitionSolution(this, file, dataset));
        }
        return population;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

}
