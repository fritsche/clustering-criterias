package br.ufpr.inf.cbio.clusteringcriterias.operator;


import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class implements a random mutation operator for partition solutions
 *
 * @author Adriano F. Kultzak <adriano_fk@hotmail.com>
 */

public class ClusterRandomMutation implements MutationOperator<PartitionSolution> {
    private double mutationProbability;
    private RandomGenerator<Double> randomGenerator ;

    /**  Constructor */
    public ClusterRandomMutation(double probability) {
        this(probability, () -> JMetalRandom.getInstance().nextDouble());
    }

    public ClusterRandomMutation(double probability, RandomGenerator<Double> randomGenerator) {
        if (probability < 0) {
            throw new JMetalException("Mutation probability is negative: " + mutationProbability) ;
        }

        this.mutationProbability = probability ;
        this.randomGenerator = randomGenerator ;
    }


    /* Getters */
    public double getMutationProbability() {
        return mutationProbability;
    }

    /* Setters */
    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    /** Execute() method */
    @Override
    public PartitionSolution execute(PartitionSolution solution) {
        if (null == solution) {
            throw new JMetalException("Null parameter") ;
        }

        doMutation(mutationProbability, solution);

        return solution;
    }

    int num = 0;
    private void doMutation(double probability, PartitionSolution solution){

        int max = solution.getVariableValue(solution.getNumberOfVariables()-1);
//        System.out.println(solution.getVariables());

        for (int i = 0; i < solution.getNumberOfVariables()-1; i++){
            if (randomGenerator.getRandomValue() <= probability) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, max);
                solution.setVariableValue(i,randomNum);
            }
        }
        ArrayList<Integer> diffNum = new ArrayList<>();

        for(int i=0; i< solution.getNumberOfVariables()-1; i++){
            if(!diffNum.contains(solution.getVariableValue(i))){
                diffNum.add(solution.getVariableValue(i));
            }
        }
        solution.setVariableValue(solution.getNumberOfVariables()-1,diffNum.size());
//        System.out.println(solution.getVariables());
//        System.out.println(diffNum.size());
//        System.out.println(num++);


    }

}
