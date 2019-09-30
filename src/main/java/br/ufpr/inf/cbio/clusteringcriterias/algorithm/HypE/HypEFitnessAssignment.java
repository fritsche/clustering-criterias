package br.ufpr.inf.cbio.clusteringcriterias.algorithm.HypE;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.solutionattribute.impl.Fitness;

import java.util.List;

/**
 *
 * @author Gian Fritsche <gian.fritsche@gmail.com>
 * @param <S>
 */
public class HypEFitnessAssignment<S extends Solution> {

	private Fitness fitness = new Fitness();

	public void setHypEFitness(List<S> population, Solution reference, int k, int nrOfSamples) {
		if (reference.getNumberOfObjectives() <= 3) {
			setExactHypEFitness(population, reference, k);
		} else {
			setEstimatedHypEFitness(population, reference, k, nrOfSamples);
		}
	}

	void setExactHypEFitness(List<S> population, Solution reference, int k) {
		HypEFitness hy = new HypEFitness();

		int objs = reference.getNumberOfObjectives();

		int size = population.size();

		double[][] points = new double[size + 1][objs + 1];

		for (int i = 1; i <= size; i++) {
			for (int j = 1; j <= objs; j++) {
				points[i][j] = population.get(i - 1).getObjective(j - 1);
			}
		}

		double[] bounds = new double[objs + 1];
		for (int i = 1; i <= objs; i++) {
			bounds[i] = reference.getObjective(i - 1);
		}

		double[] result = hy.hypeIndicatorExact(points, bounds, k);

		for (int i = 1; i <= size; i++) {
			fitness.setAttribute(population.get(i - 1), result[i]);
		}

	}

	void setEstimatedHypEFitness(List<S> population,
                                 Solution reference, int k, int nrOfSamples) {

		HypEFitness hy = new HypEFitness();

		int objs = reference.getNumberOfObjectives();

		int size = population.size();

		double[][] points = new double[size][objs];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < objs; j++) {
				points[i][j] = population.get(i).getObjective(j);
			}
		}

		double lowerbound = 0;
		double upperbound = reference.getObjective(0);

		double[] result = hy.hypeIndicatorSampled(points, lowerbound, upperbound, k,
				nrOfSamples);

		for (int i = 0; i < size; i++) {
			fitness.setAttribute(population.get(i), result[i]);
		}

	}
}