package br.ufpr.inf.cbio.clusteringcriterias.operator;


import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import br.ufpr.inf.cbio.clusteringcriterias.problem.PartitionCentroids;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.util.distance.EuclideanDistance;
import org.uma.jmetal.util.point.util.distance.PointDistance;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * This class implements a centroid based mutation operator for partition solutions
 * If a object is select it will be labed according to the closest cluster centroid
 * with a probability of 1/N
 *
 * @author Adriano F. Kultzak <adriano_fk@hotmail.com>
 */

public class CentroidBasedMutation implements MutationOperator<PartitionSolution> {
    private double mutationProbability;
    private RandomGenerator<Double> randomGenerator ;
    private Dataset dataset;

    /**  Constructor */
    public CentroidBasedMutation(Dataset dataset) {
        this(() -> JMetalRandom.getInstance().nextDouble());
        this.dataset  = dataset;
    }

    public CentroidBasedMutation(RandomGenerator<Double> randomGenerator) {
        double probability = 1;
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

        probability = probability/(solution.getNumberOfVariables()-1);


        PointDistance distance = new EuclideanDistance();
        double sum;
        int max = solution.getVariableValue(solution.getNumberOfVariables()-1);
        PartitionCentroids partitionCentroids = new PartitionCentroids();
        partitionCentroids.computeCentroids(solution, dataset);
        Map<Integer, Point> centroids = (new PartitionCentroids()).getAttribute(solution);

        for (int i = 0; i < solution.getNumberOfVariables()-1; i++){
            if (randomGenerator.getRandomValue() <= probability) {
                sum = Double.POSITIVE_INFINITY;
                int cluster = solution.getVariableValue(i);
                for(int j = 0; j < solution.getVariableValue(solution.getNumberOfVariables()-1); j++){
                    if(sum > distance.compute(dataset.getPoint(i), centroids.get(j))){
                        sum = distance.compute(dataset.getPoint(i), centroids.get(j));
                        cluster = j;
                    }
                }
                solution.setVariableValue(i,cluster);
            }
        }

        //conta o novo número de clusters
        ArrayList<Integer> diffNum = new ArrayList<>();
        for(int i=0; i< solution.getNumberOfVariables()-1; i++){
            if(!diffNum.contains(solution.getVariableValue(i))){
                diffNum.add(solution.getVariableValue(i));
            }
        }
        solution.setVariableValue(solution.getNumberOfVariables()-1,diffNum.size());

        //corrige os gaps caso existam na solução mutada
        Collections.sort(diffNum);
        for (int i = 0; i < diffNum.size(); i++){
            if(diffNum.get(i) != diffNum.indexOf(diffNum.get(i))){
                for(int i1 = 0; i1 < solution.getNumberOfVariables()-1; i1++){
                    solution.setVariableValue(i1,diffNum.indexOf(solution.getVariableValue(i1)));
                }
                break;
            }
        }
    }

}
