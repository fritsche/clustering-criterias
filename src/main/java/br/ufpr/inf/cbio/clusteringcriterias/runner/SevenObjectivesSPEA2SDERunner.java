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

import br.ufpr.inf.cbio.clusteringcriterias.algorithm.SPEA2SDE.CLUSPEA2SDEBuilder;
import br.ufpr.inf.cbio.clusteringcriterias.algorithm.SPEA2SDE.SPEA2SDE;
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
import br.ufpr.inf.cbio.clusteringcriterias.operator.HBGFCrossover;
import br.ufpr.inf.cbio.clusteringcriterias.problem.ClusterProblem;
import br.ufpr.inf.cbio.clusteringcriterias.problem.Utils;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import java.util.ArrayList;
import java.util.List;
import jep.JepException;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.mutation.NullMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class SevenObjectivesSPEA2SDERunner {

    public void execute() throws JepException {

        Dataset dataset = DatasetFactory.getInstance().getNormalizedDataset(DatasetFactory.DATASET.D31.toString());

        List<ObjectiveFunction> functions = new ArrayList<>();
        functions.add(new Connectivity(Utils.computeNeighborhood(dataset.getDistanceMatrix())));
        // functions.add(new DaviesBouldin(dataset));
        functions.add(new MinimizationSilhouette(dataset));
        functions.add(new InvertedMinimumCentroidDistance(dataset));
        // functions.add(new MinimizationSeparation(dataset));
        // functions.add(new MaximumDiameter(dataset));
        functions.add(new OverallDeviation(dataset));
        Problem problem = new ClusterProblem(true, dataset, functions);

        CrossoverOperator<PartitionSolution> crossover;
        MutationOperator<PartitionSolution> mutation;
        crossover = new HBGFCrossover();
        mutation = new NullMutation<>();
        SelectionOperator<List<PartitionSolution>, PartitionSolution> selection;
        selection = new BinaryTournamentSelection<>();

        int popSize = ((ClusterProblem) problem).getPopulationSize();
        SPEA2SDE spea2sde = new CLUSPEA2SDEBuilder<>(problem)
                .setCrossoverOperator(crossover)
                .setMutationOperator(mutation)
                .setPopulationSize(popSize + (popSize % 2))
                .setMaxIterations(51)
                .setSelectionOperator(selection)
                .build();

        spea2sde.run();

        List<PartitionSolution> result = spea2sde.getResult();

        List<PartitionSolution> initial = new ArrayList<>(popSize);
        for (int i = 0; i < popSize; i++) {
            initial.add((PartitionSolution) problem.createSolution());
        }

        int countnew = 0;
        for (PartitionSolution pres : result) {
            boolean contains = false;
            for (PartitionSolution pini : initial) {
                boolean equals = true;
                for (int i = 0; i < pini.getNumberOfObjectives(); i++) {
                    if (pres.getObjective(i) != pini.getObjective(i)) {
                        equals = false;
                        break;
                    }
                }
                if (equals) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                countnew++;
            }
        }
        System.out.println(countnew + " newly generated solutions. [" + functions.size() + " objectives]");
    }

    public static void main(String[] args) throws JepException {
        JMetalRandom.getInstance().setSeed(420621032);
        (new SevenObjectivesSPEA2SDERunner()).execute();
    }
}
