package br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders;

import br.ufpr.inf.cbio.clusteringcriterias.operator.HBGFCrossover;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import org.uma.jmetal.algorithm.multiobjective.ibea.IBEA;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.mutation.NullMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AlgorithmBuilder;

import java.util.List;

/**
 * This class implements the IBEA algorithm
 */
public class CLUIBEABuilder implements AlgorithmBuilder<IBEA<PartitionSolution>> {
	private Problem<PartitionSolution> problem;
	private int populationSize;
	private int archiveSize;
	private int maxEvaluations;

	private CrossoverOperator<PartitionSolution> crossover;
	private MutationOperator<PartitionSolution> mutation;
	private SelectionOperator<List<PartitionSolution>, PartitionSolution> selection;

	/**
	 * Constructor
	 * @param problem
	 */
	public CLUIBEABuilder(Problem<PartitionSolution> problem) {
		this.problem = problem;
		populationSize = 100;
		archiveSize = 100;
		maxEvaluations = 25000;

		double crossoverProbability = 1.0;
		int numberOfGeneratedChild = 2;
		crossover = new HBGFCrossover(crossoverProbability, numberOfGeneratedChild);

		mutation = new NullMutation<>();

		selection = new BinaryTournamentSelection<PartitionSolution>();
	}

	/* Getters */
	public int getPopulationSize() {
		return populationSize;
	}

	public int getArchiveSize() {
		return archiveSize;
	}

	public int getMaxEvaluations() {
		return maxEvaluations;
	}

	public CrossoverOperator<PartitionSolution> getCrossover() {
		return crossover;
	}

	public MutationOperator<PartitionSolution> getMutation() {
		return mutation;
	}

	public SelectionOperator<List<PartitionSolution>, PartitionSolution> getSelection() {
		return selection;
	}

	/* Setters */
	public CLUIBEABuilder setPopulationSize(int populationSize) {
		this.populationSize = populationSize;

		return this;
	}

	public CLUIBEABuilder setArchiveSize(int archiveSize) {
		this.archiveSize = archiveSize;

		return this;
	}

	public CLUIBEABuilder setMaxEvaluations(int maxEvaluations) {
		this.maxEvaluations = maxEvaluations;

		return this;
	}

	public CLUIBEABuilder setCrossover(CrossoverOperator<PartitionSolution> crossover) {
		this.crossover = crossover;

		return this;
	}

	public CLUIBEABuilder setMutation(MutationOperator<PartitionSolution> mutation) {
		this.mutation = mutation;

		return this;
	}

	public CLUIBEABuilder setSelection(SelectionOperator<List<PartitionSolution>, PartitionSolution> selection) {
		this.selection = selection;

		return this;
	}

	public IBEA<PartitionSolution> build() {
		return new IBEA<PartitionSolution>(problem, populationSize, archiveSize, maxEvaluations, selection, crossover,
				mutation);
	}
}

