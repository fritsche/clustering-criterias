package br.ufpr.inf.cbio.clusteringcriterias.algorithm.SPEA2SDE;

import org.uma.jmetal.solution.Solution;

import java.util.List;

/**
 *
 * @author Gian Fritsche <gian.fritsche at gmail.com>
 */
public class SPEA2SDEUtils {

	public static <S extends Solution<?>> double[][] distanceMatrixSDE(List<S> solutionSet) {
		// The matrix of distances
		double[][] distance = new double[solutionSet.size()][solutionSet.size()];
		// -> Calculate the distances
		for (int i = 0; i < solutionSet.size(); i++) {
			for (int j = 0; j < solutionSet.size(); j++) {
				if (j == i) {
					distance[i][j] = 0;
				} else {
					distance[i][j] = distanceBetweenObjectivesSDE(
							solutionSet.get(i), solutionSet.get(j));
				}
			}
		} // for

		// ->Return the matrix of distances
		return distance;

	}

	public static double distanceBetweenObjectivesSDE(Solution ind1, Solution ind2) {
		int i;
		double d = 0.0;
		int nobj = ind1.getNumberOfObjectives();

		for (i = 0; i < nobj; i++) {
			if (ind1.getObjective(i) < ind2.getObjective(i)) {
				d += (ind2.getObjective(i) - ind1.getObjective(i))
						* (ind2.getObjective(i) - ind1.getObjective(i));
			}
		}
		return Math.sqrt(d);
	}

}