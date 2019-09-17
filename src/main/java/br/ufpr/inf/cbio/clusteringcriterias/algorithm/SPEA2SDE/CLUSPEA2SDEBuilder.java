package br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.SPEA2SDE;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import java.util.List;

/**
 *
 * @author Gian Fritsche <gmfritsche@inf.ufpr.br>
 * @param <S>
 */
public class CLUSPEA2SDEBuilder<S extends Solution<?>> implements AlgorithmBuilder<SPEA2SDE<S>> {

	protected final Problem<S> problem;
	private int maxIterations;
	private int populationSize;
	private CrossoverOperator<S> crossoverOperator;
	private MutationOperator<S> mutationOperator;
	private SelectionOperator<List<S>, S> selectionOperator;
	private SolutionListEvaluator<S> evaluator;

	public CLUSPEA2SDEBuilder(Problem<S> problem) {
		this.problem = problem;
		selectionOperator = new BinaryTournamentSelection<>();
		evaluator = new SequentialSolutionListEvaluator<>();
	}

	@Override
	public SPEA2SDE<S> build() {
		return new SPEA2SDE<>(problem, maxIterations, populationSize, crossoverOperator, mutationOperator, selectionOperator, evaluator);
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public CLUSPEA2SDEBuilder setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
		return this;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public CLUSPEA2SDEBuilder setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
		return this;
	}

	public CrossoverOperator<S> getCrossoverOperator() {
		return crossoverOperator;
	}

	public CLUSPEA2SDEBuilder setCrossoverOperator(CrossoverOperator<S> crossoverOperator) {
		this.crossoverOperator = crossoverOperator;
		return this;
	}

	public MutationOperator<S> getMutationOperator() {
		return mutationOperator;
	}

	public CLUSPEA2SDEBuilder setMutationOperator(MutationOperator<S> mutationOperator) {
		this.mutationOperator = mutationOperator;
		return this;
	}

	public SelectionOperator<List<S>, S> getSelectionOperator() {
		return selectionOperator;
	}

	public CLUSPEA2SDEBuilder setSelectionOperator(SelectionOperator<List<S>, S> selectionOperator) {
		this.selectionOperator = selectionOperator;
		return this;
	}

	public SolutionListEvaluator<S> getEvaluator() {
		return evaluator;
	}

	public CLUSPEA2SDEBuilder setEvaluator(SolutionListEvaluator<S> evaluator) {
		this.evaluator = evaluator;
		return this;
	}

}
