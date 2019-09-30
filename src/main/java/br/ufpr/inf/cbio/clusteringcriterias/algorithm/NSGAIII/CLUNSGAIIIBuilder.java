package br.ufpr.inf.cbio.clusteringcriterias.algorithm.NSGAIII;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;

/**
 *
 * @author Gian Fritsche <gmfritsche@inf.ufpr.br>
 * @param <S>
 */
public class CLUNSGAIIIBuilder<S extends Solution> implements AlgorithmBuilder<NSGAIII<S>> {

    private final Problem<S> problem;
    private int maxEvaluations;
    private boolean normalize;
    private int populationSize;
    private CrossoverOperator crossover;
    private MutationOperator mutation;
    private SelectionOperator selection;

    public CLUNSGAIIIBuilder(Problem<S> problem, CrossoverOperator<S> crossover, MutationOperator<S> mutation) {
        this.problem = problem;
        this.mutation = mutation;
        this.crossover = crossover;
    }

    public Problem getProblem() {
        return this.problem;
    }

    public CLUNSGAIIIBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
        return this;
    }

    public CLUNSGAIIIBuilder setNormalize(boolean normalize) {
        this.normalize = normalize;
        return this;
    }

    public CLUNSGAIIIBuilder setCrossover(CrossoverOperator crossover) {
        this.crossover = crossover;
        return this;
    }

    public CLUNSGAIIIBuilder setMutation(MutationOperator mutation) {
        this.mutation = mutation;
        return this;
    }

    public CLUNSGAIIIBuilder setSelection(SelectionOperator selection) {
        this.selection = selection;
        return this;
    }

    public CLUNSGAIIIBuilder setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    @Override
    public NSGAIII build() {
        return new NSGAIII(this);
    }

    public int getMaxEvaluations() {
        return this.maxEvaluations;
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

    public MutationOperator getMutation() {
        return this.mutation;
    }

    public SelectionOperator getSelection() {
        return this.selection;
    }
}