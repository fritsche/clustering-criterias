package br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.MOEAD;

import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;

import java.util.ArrayList;
import java.util.List;

public class CLUMOEAD extends AbstractMOEAD<PartitionSolution> {

	public CLUMOEAD(Problem<PartitionSolution> problem,
					int populationSize,
					int resultPopulationSize,
					int maxEvaluations,
					MutationOperator<PartitionSolution> mutation,
					CrossoverOperator<PartitionSolution> crossover,
					FunctionType functionType,
					String dataDirectory,
					double neighborhoodSelectionProbability,
					int maximumNumberOfReplacedSolutions,
					int neighborSize) {
		super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
				dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
				neighborSize);

	}

	@Override
	public void run() {
		initializePopulation() ;
		initializeUniformWeight();
		initializeNeighborhood();
		idealPoint.update(population); ;

		evaluations = populationSize ;
		do {
			int[] permutation = new int[populationSize];
			MOEADUtils.randomPermutation(permutation, populationSize);

			for (int i = 0; i < populationSize; i++) {
				int subProblemId = permutation[i];

				NeighborType neighborType = chooseNeighborType() ;
				List<PartitionSolution> parents = parentSelection(subProblemId, neighborType) ;

				parents.remove(2);

				List<PartitionSolution> children = crossoverOperator.execute(parents);

				PartitionSolution child = children.get(0) ;
				mutationOperator.execute(child);
				problem.evaluate(child);

				evaluations++;

				idealPoint.update(child.getObjectives());
				updateNeighborhood(child, subProblemId, neighborType);
			}
		} while (evaluations < maxEvaluations);

	}

	private void initializePopulation() {    population = new ArrayList<>(populationSize);
		for (int i = 0; i < populationSize; i++) {
			PartitionSolution newSolution = (PartitionSolution) problem.createSolution();

			problem.evaluate(newSolution);
			population.add(newSolution);
		}
	}

	@Override public String getName() {
		return "MOEAD" ;
	}

	@Override public String getDescription() {
		return "Multi-Objective Evolutionary Algorithm based on Decomposition" ;
	}
}
