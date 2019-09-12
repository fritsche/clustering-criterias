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
package br.ufpr.inf.cbio.clusteringcriterias.runner;

import br.ufpr.inf.cbio.clusteringcriterias.criterias.ObjectiveFunction;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.Connectivity;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.DaviesBouldin;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.InvertedMinimumCentroidDistance;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.MaximumDiameter;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.MinimizationSeparation;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.MinimizationSilhouette;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.OverallDeviation;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.DatasetFactory;
import br.ufpr.inf.cbio.clusteringcriterias.problem.ClusterProblem;
import br.ufpr.inf.cbio.clusteringcriterias.problem.Utils;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jep.JepException;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class DataExtractor {

    public static void main(String[] args) throws JepException {
        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.D31.toString());
        List<ObjectiveFunction> functions = new ArrayList<>();
        functions.add(new Connectivity(Utils.computeNeighborhood(dataset.getDistanceMatrix())));
        functions.add(new DaviesBouldin(dataset));
        functions.add(new InvertedMinimumCentroidDistance(dataset));
        functions.add(new MaximumDiameter(dataset));
        functions.add(new MinimizationSeparation(dataset));
        functions.add(new MinimizationSilhouette(dataset));
        functions.add(new OverallDeviation(dataset));
        ClusterProblem problem = new ClusterProblem(true, dataset, functions);
        int popSize = problem.getPopulationSize();
        for (int i = 0; i < popSize; i++) {
            PartitionSolution solution = problem.createSolution();
            problem.evaluate(solution);
            double[] objectives = solution.getObjectives();
            System.out.println(Arrays.toString(objectives));
        }
    }
}
