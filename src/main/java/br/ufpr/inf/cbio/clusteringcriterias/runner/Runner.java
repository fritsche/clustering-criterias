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
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.OverallDeviation;
import br.ufpr.inf.cbio.clusteringcriterias.problem.ClusterProblem;
import br.ufpr.inf.cbio.clusteringcriterias.problem.DataSet;
import br.ufpr.inf.cbio.clusteringcriterias.problem.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.NullMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.point.util.distance.EuclideanDistance;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class Runner {
    
    public void run() {
        
        String dataSetPath = "clustering/iris/iris-dataset.txt";
        String initialPartitionsPath = "clustering/iris/initialPartitions/";
        DataSet dataSet = new DataSet(new File(getClass().getClassLoader().getResource(dataSetPath).getFile()));
        
        int minK = 2;
        int maxK = 5;
        int popSize = 10;
        int maxFitnessEvaluations = popSize * 50;
        
        double crossoverProbability;
        double crossoverDistributionIndex;
        Problem problem;
        CrossoverOperator<IntegerSolution> crossover;
        MutationOperator<IntegerSolution> mutation;
        SelectionOperator<List<IntegerSolution>, IntegerSolution> selection;
        
        List<ObjectiveFunction> functions = new ArrayList<>();
        functions.add(new OverallDeviation(dataSet, new EuclideanDistance()));
        functions.add(new Connectivity(Utils.computeNeighborhood(Utils.computeDistanceMatrix(dataSet, new EuclideanDistance()), maxK)));
        
        problem = new ClusterProblem(true, dataSet, functions, Utils.getInitialPartitionFiles(initialPartitionsPath));
        
        crossoverProbability = 0.9;
        crossoverDistributionIndex = 20.0;
        crossover = new IntegerSBXCrossover(crossoverProbability, crossoverDistributionIndex);
        
        mutation = new NullMutation<>();
        
        selection = new BinaryTournamentSelection<>(
                new RankingAndCrowdingDistanceComparator<IntegerSolution>());
        
        Algorithm<List<IntegerSolution>> algorithm = new NSGAIIBuilder<>(problem, crossover, mutation)
                .setSelectionOperator(selection)
                .setMaxEvaluations(maxFitnessEvaluations)
                .setPopulationSize(popSize + (popSize % 2))
                .build();
        
        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute();
        
        long computingTime = algorithmRunner.getComputingTime();
        JMetalLogger.logger.log(Level.INFO, "Total execution time: {0}ms", computingTime);
        
        List<IntegerSolution> population = SolutionListUtils.getNondominatedSolutions(algorithm.getResult());
        
        for (IntegerSolution s : population) {
            System.out.println(s);
        }
        
    }
    
    public static void main(String[] args) {
        (new Runner()).run();
    }
    
}
