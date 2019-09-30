package br.ufpr.inf.cbio.clusteringcriterias.algorithm.NSGAIII;

import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * Class providing int [] permutations
 */
public class Permutation {

    /**
     * Return a permutation vector between the 0 and (length - 1)
     *
     * @param length
     * @return
     */
    public int[] intPermutation(int length) {
        int[] aux = new int[length];
        int[] result = new int[length];
        JMetalRandom randomGenerator = JMetalRandom.getInstance();
        // First, create an array from 0 to length - 1. We call them result
        // Also is needed to create an random array of size length
        for (int i = 0; i < length; i++) {
            result[i] = i;
            aux[i] = randomGenerator.nextInt(0, length - 1);
        } // for

        // Sort the random array with effect in result, and then we obtain a
        // permutation array between 0 and length - 1
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                if (aux[i] > aux[j]) {
                    int tmp;
                    tmp = aux[i];
                    aux[i] = aux[j];
                    aux[j] = tmp;
                    tmp = result[i];
                    result[i] = result[j];
                    result[j] = tmp;
                } // if
            } // for
        } // for
        return result;
    }
} // Permutation
