package br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders;

import br.ufpr.inf.cbio.clusteringcriterias.operator.HBGFCrossover;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEAD;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.NullMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AlgorithmBuilder;

/**
 * Builder class for algorithm MOEA/D and variants
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
public class CLUMOEADBuilder implements AlgorithmBuilder<AbstractMOEAD<PartitionSolution>> {
	public enum Variant {MOEAD, ConstraintMOEAD, MOEADDRA, MOEADSTM, MOEADD} ;

	protected Problem<PartitionSolution> problem ;

	/** T in Zhang & Li paper */
	protected int neighborSize;
	/** Delta in Zhang & Li paper */
	protected double neighborhoodSelectionProbability;
	/** nr in Zhang & Li paper */
	protected int maximumNumberOfReplacedSolutions;

	protected MOEAD.FunctionType functionType;

	protected CrossoverOperator<PartitionSolution> crossover;
	protected MutationOperator<PartitionSolution> mutation;
	protected String dataDirectory;

	protected int populationSize;
	protected int resultPopulationSize ;

	protected int maxEvaluations;

	protected int numberOfThreads ;

	protected br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder.Variant moeadVariant ;

	/** Constructor */
	public CLUMOEADBuilder(Problem<PartitionSolution> problem, br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder.Variant variant) {
		this.problem = problem ;
		populationSize = 300 ;
		resultPopulationSize = 300 ;
		maxEvaluations = 150000 ;
		crossover = new HBGFCrossover(1, 2) ;
		mutation = new NullMutation<>();
		functionType = MOEAD.FunctionType.TCHE ;
		neighborhoodSelectionProbability = 0.1 ;
		maximumNumberOfReplacedSolutions = 2 ;
		dataDirectory = "" ;
		neighborSize = 20 ;
		numberOfThreads = 1 ;
		moeadVariant = variant ;
	}

	/* Getters/Setters */
	public int getNeighborSize() {
		return neighborSize;
	}

	public int getMaxEvaluations() {
		return maxEvaluations;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public int getResultPopulationSize() {
		return resultPopulationSize;
	}

	public String getDataDirectory() {
		return dataDirectory;
	}

	public MutationOperator<PartitionSolution> getMutation() {
		return mutation;
	}

	public CrossoverOperator<PartitionSolution> getCrossover() {
		return crossover;
	}

	public MOEAD.FunctionType getFunctionType() {
		return functionType;
	}

	public int getMaximumNumberOfReplacedSolutions() {
		return maximumNumberOfReplacedSolutions;
	}

	public double getNeighborhoodSelectionProbability() {
		return neighborhoodSelectionProbability;
	}

	public int getNumberOfThreads() {
		return numberOfThreads ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder setPopulationSize(int populationSize) {
		this.populationSize = populationSize;

		return this;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder setResultPopulationSize(int resultPopulationSize) {
		this.resultPopulationSize = resultPopulationSize;

		return this;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder setMaxEvaluations(int maxEvaluations) {
		this.maxEvaluations = maxEvaluations;

		return this;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder setNeighborSize(int neighborSize) {
		this.neighborSize = neighborSize ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder setNeighborhoodSelectionProbability(double neighborhoodSelectionProbability) {
		this.neighborhoodSelectionProbability = neighborhoodSelectionProbability ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder setFunctionType(MOEAD.FunctionType functionType) {
		this.functionType = functionType ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder setMaximumNumberOfReplacedSolutions(int maximumNumberOfReplacedSolutions) {
		this.maximumNumberOfReplacedSolutions = maximumNumberOfReplacedSolutions ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder setCrossover(CrossoverOperator<PartitionSolution> crossover) {
		this.crossover = crossover ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder setMutation(MutationOperator<PartitionSolution> mutation) {
		this.mutation = mutation ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder setDataDirectory(String dataDirectory) {
		this.dataDirectory = dataDirectory ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads ;

		return this ;
	}

	public AbstractMOEAD<PartitionSolution> build() {
		AbstractMOEAD<PartitionSolution> algorithm = null ;
		if (moeadVariant.equals(br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder.Variant.MOEAD)) {
			algorithm = new CLUMOEAD(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
					crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
					maximumNumberOfReplacedSolutions, neighborSize);
		} /*else if (moeadVariant.equals(br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder.Variant.ConstraintMOEAD)) {
			algorithm =  new ConstraintMOEAD(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
					crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
					maximumNumberOfReplacedSolutions, neighborSize);
		} else if (moeadVariant.equals(br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder.Variant.MOEADDRA)) {
			algorithm =  new MOEADDRA(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
					crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
					maximumNumberOfReplacedSolutions, neighborSize);
		} else if (moeadVariant.equals(br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder.Variant.MOEADSTM)) {
			algorithm =  new MOEADSTM(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
					crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
					maximumNumberOfReplacedSolutions, neighborSize);
		} */else if (moeadVariant.equals(br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUMOEADBuilder.Variant.MOEADD)) {
			algorithm = new CLUMOEADD(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
					functionType, dataDirectory, neighborhoodSelectionProbability,
					maximumNumberOfReplacedSolutions, neighborSize);
		}
		return algorithm ;
	}
}

