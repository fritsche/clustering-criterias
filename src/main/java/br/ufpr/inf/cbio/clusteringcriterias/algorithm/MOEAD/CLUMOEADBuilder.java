package br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.MOEAD;

import br.ufpr.inf.cbio.clusteringcriterias.operator.HBGFCrossover;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
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

	protected CLUMOEAD.FunctionType functionType;

	protected CrossoverOperator<PartitionSolution> crossover;
	protected MutationOperator<PartitionSolution> mutation;
	protected String dataDirectory;

	protected int populationSize;
	protected int resultPopulationSize ;

	protected int maxEvaluations;

	protected int numberOfThreads ;

	protected Variant moeadVariant ;

	/** Constructor */
	public CLUMOEADBuilder(Problem<PartitionSolution> problem, Variant variant) {
		this.problem = problem ;
		resultPopulationSize = 300 ;
		maxEvaluations = 150000 ;
		crossover = new HBGFCrossover() ;
		mutation = new NullMutation<>();
		functionType = CLUMOEAD.FunctionType.AGG ;
		neighborhoodSelectionProbability = 0.1 ;
		maximumNumberOfReplacedSolutions = 1 ;
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

	public CLUMOEAD.FunctionType getFunctionType() {
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

	public CLUMOEADBuilder setPopulationSize(int populationSize) {
		this.populationSize = populationSize;

		return this;
	}

	public CLUMOEADBuilder setResultPopulationSize(int resultPopulationSize) {
		this.resultPopulationSize = resultPopulationSize;

		return this;
	}

	public CLUMOEADBuilder setMaxEvaluations(int maxEvaluations) {
		this.maxEvaluations = maxEvaluations;

		return this;
	}

	public CLUMOEADBuilder setNeighborSize(int neighborSize) {
		this.neighborSize = neighborSize ;

		return this ;
	}

	public CLUMOEADBuilder setNeighborhoodSelectionProbability(double neighborhoodSelectionProbability) {
		this.neighborhoodSelectionProbability = neighborhoodSelectionProbability ;

		return this ;
	}

	public CLUMOEADBuilder setFunctionType(CLUMOEAD.FunctionType functionType) {
		this.functionType = functionType ;

		return this ;
	}

	public CLUMOEADBuilder setMaximumNumberOfReplacedSolutions(int maximumNumberOfReplacedSolutions) {
		this.maximumNumberOfReplacedSolutions = maximumNumberOfReplacedSolutions ;

		return this ;
	}

	public CLUMOEADBuilder setCrossover(CrossoverOperator<PartitionSolution> crossover) {
		this.crossover = crossover ;

		return this ;
	}

	public CLUMOEADBuilder setMutation(MutationOperator<PartitionSolution> mutation) {
		this.mutation = mutation ;

		return this ;
	}

	public CLUMOEADBuilder setDataDirectory(String dataDirectory) {
		this.dataDirectory = dataDirectory ;

		return this ;
	}

	public CLUMOEADBuilder setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads ;

		return this ;
	}

	public AbstractMOEAD<PartitionSolution> build() {
		AbstractMOEAD<PartitionSolution> algorithm = null ;
		if (moeadVariant.equals(Variant.MOEAD)) {
			algorithm = new CLUMOEAD(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
					crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
					maximumNumberOfReplacedSolutions, neighborSize);
		}else if (moeadVariant.equals(br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.MOEAD.CLUMOEADBuilder.Variant.MOEADSTM)) {
			algorithm =  new CLUMOEADSTM(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
					crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
					maximumNumberOfReplacedSolutions, neighborSize);
		} else if (moeadVariant.equals(Variant.MOEADD)) {
			algorithm = new CLUMOEADD(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
					functionType, dataDirectory, neighborhoodSelectionProbability,
					maximumNumberOfReplacedSolutions, neighborSize);
		}
		return algorithm ;
	}
}

