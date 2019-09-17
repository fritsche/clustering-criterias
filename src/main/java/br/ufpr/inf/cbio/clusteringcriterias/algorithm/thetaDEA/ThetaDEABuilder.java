package br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.thetaDEA;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;

/**
 *
 * @author Gian Fritsche <gmfritsche@inf.ufpr.br>
 * @param <S>
 */
public class ThetaDEABuilder<S extends Solution> implements AlgorithmBuilder<ThetaDEA<S>> {

	private final Problem<S> problem;
	private int maxGenerations;
	private double theta;
	private boolean normalize;
	private int populationSize;
	private CrossoverOperator crossover;
	private MutationOperator mutation;

	public ThetaDEABuilder(Problem<S> problem) {
		this.problem = problem;
	}

	public Problem getProblem() {
		return this.problem;
	}

	public ThetaDEABuilder setMaxGenerations(int maxGenerations) {
		this.maxGenerations = maxGenerations;
		return this;
	}

	public ThetaDEABuilder setTheta(double theta) {
		this.theta = theta;
		return this;
	}

	public ThetaDEABuilder setNormalize(boolean normalize) {
		this.normalize = normalize;
		return this;
	}

	public ThetaDEABuilder setCrossover(CrossoverOperator crossover) {
		this.crossover = crossover;
		return this;
	}

	public ThetaDEABuilder setMutation (MutationOperator mutation) {
		this.mutation = mutation;
		return this;
	}

	public ThetaDEABuilder setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
		return this;
	}

	@Override
	public ThetaDEA build() {
		return new ThetaDEA(this);
	}

	public int getMaxGenerations() {
		return this.maxGenerations;
	}

	public double getTheta() {
		return this.theta;
	}

	public boolean getNormalize() {
		return this.normalize;
	}

	public int getPopulationSize() {
		return this.populationSize;
	}

	public CrossoverOperator getCrossover() {
		return this.crossover;
	}

	public MutationOperator getMutation(){
		return this.mutation;
	}
}