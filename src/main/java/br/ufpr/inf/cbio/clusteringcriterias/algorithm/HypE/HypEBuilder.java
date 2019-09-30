package br.ufpr.inf.cbio.clusteringcriterias.algorithm.HypE;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;

import java.util.List;

/**
 * Based on the source received from Lei Cai <caileid at gmail.com>
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 * @param <S>
 */
public class HypEBuilder<S extends Solution<?>> implements AlgorithmBuilder<HypE<S>> {

	private final Problem<S> problem;
	private int populationSize;
	private int maxEvaluations;
	private int samples;
	private CrossoverOperator<S> crossoverOperator;
	private MutationOperator<S> mutationOperator;
	private SelectionOperator<List<S>, S> selectionOperator;

	public HypEBuilder(Problem<S> problem) {
		this.problem = problem;
	}

	@Override
	public HypE<S> build() {
		return new HypE(this);
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public HypEBuilder setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
		return this;
	}

	public int getMaxEvaluations() {
		return maxEvaluations;
	}

	public HypEBuilder setMaxEvaluations(int maxEvaluations) {
		this.maxEvaluations = maxEvaluations;
		return this;
	}

	public int getSamples() {
		return samples;
	}

	public HypEBuilder setSamples(int samples) {
		this.samples = samples;
		return this;
	}

	public CrossoverOperator<S> getCrossoverOperator() {
		return crossoverOperator;
	}

	public HypEBuilder setCrossoverOperator(CrossoverOperator<S> crossoverOperator) {
		this.crossoverOperator = crossoverOperator;
		return this;
	}

	public MutationOperator<S> getMutationOperator() {
		return mutationOperator;
	}

	public HypEBuilder setMutationOperator(MutationOperator<S> mutationOperator) {
		this.mutationOperator = mutationOperator;
		return this;
	}

	public SelectionOperator<List<S>, S> getSelectionOperator() {
		return selectionOperator;
	}

	public HypEBuilder setSelectionOperator(SelectionOperator<List<S>, S> selectionOperator) {
		this.selectionOperator = selectionOperator;
		return this;
	}

	public Problem<S> getProblem() {
		return problem;
	}

}
