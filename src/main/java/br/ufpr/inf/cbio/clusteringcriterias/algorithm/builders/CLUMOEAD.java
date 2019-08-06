package br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders;

import br.ufpr.inf.cbio.clusteringcriterias.operator.HBGFCrossover;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;

import java.util.ArrayList;
import java.util.List;

//import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;

/**
 * Class implementing the MOEA/D-DE algorithm described in :
 * Hui Li; Qingfu Zhang, "Multiobjective Optimization Problems With Complicated Pareto Sets,
 * MOEA/D and NSGA-II," Evolutionary Computation, IEEE Transactions on , vol.13, no.2, pp.284,302,
 * April 2009. doi: 10.1109/TEVC.2008.925798
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
@SuppressWarnings("serial")
public class CLUMOEAD extends AbstractMOEAD<PartitionSolution> {
	protected HBGFCrossover hbgfCrossover;

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

		hbgfCrossover = (HBGFCrossover) crossoverOperator ;
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

//				System.out.println(parents);
				parents.remove(0); //removido o ppopulation.get(subProblemId)

//				hbgfCrossover.setCurrentSolution(population.get(subProblemId)); //todo: verificar o que essa linha faz
				List<PartitionSolution> children = hbgfCrossover.execute(parents);

				PartitionSolution child = children.get(0) ;
				mutationOperator.execute(child);
				problem.evaluate(child);

				evaluations++;

				idealPoint.update(child.getObjectives());
				updateNeighborhood(child, subProblemId, neighborType);
			}
		} while (evaluations < maxEvaluations);

	}

	protected void initializePopulation() {
		population = new ArrayList<>(populationSize);
		for (int i = 0; i < populationSize; i++) {
			PartitionSolution newSolution = (PartitionSolution)problem.createSolution();

			problem.evaluate(newSolution);
			population.add(newSolution);
		}
	}

	@Override
    public String getName() {
		return "MOEAD" ;
	}

	@Override
    public String getDescription() {
		return "Multi-Objective Evolutionary Algorithm based on Decomposition" ;
	}
}
