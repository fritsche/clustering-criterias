package br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.thetaDEA;

import org.uma.jmetal.solution.Solution;

import java.util.Comparator;

/**
 *
 * @author Gian Fritsche <gmfritsche@inf.ufpr.br>
 * @param <S>
 */
public class FitnessComparator<S extends Solution> implements Comparator {


	private final boolean ascendingOrder_;


	public FitnessComparator() {
		ascendingOrder_ = true;
	}

	public FitnessComparator(boolean descendingOrder) {
		ascendingOrder_ = !descendingOrder;
	}

	@Override
	public int compare(Object o1, Object o2) {
		if (o1 == null)
			return 1;
		else if (o2 == null)
			return -1;

		double f1 = (double) ((S) o1).getAttribute("Fitness");
		double f2 = (double) ((S) o2).getAttribute("Fitness");
		if (ascendingOrder_) {
			if (f1 < f2) {
				return -1;
			} else if (f1 > f2) {
				return 1;
			} else {
				return 0;
			}
		} else {
			if (f1 < f2) {
				return 1;
			} else if (f1 > f2) {
				return -1;
			} else {
				return 0;
			}
		}
	} // compare
}