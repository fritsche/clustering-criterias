package br.ufpr.inf.cbio.clusteringcriterias.runner.experiment;


import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import br.ufpr.inf.cbio.clusteringcriterias.problem.Utils;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import java.io.File;
import java.util.List;

/**
 * Class defining tasks for the execution of algorithms in parallel.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ExperimentAlgorithmMOCLE<S extends Solution<?>, Result>  {
	private Algorithm<Result> algorithm;
	private String algorithmTag;
	private String problemTag;
	private String referenceParetoFront;
	private Dataset dataset;
	private int runId ;

	/**
	 * Constructor
	 */
	public ExperimentAlgorithmMOCLE(
			Algorithm<Result> algorithm,
			String algorithmTag,
			ExperimentProblem<S> problem,
			Dataset dataset,
			int runId) {
		this.algorithm = algorithm;
		this.algorithmTag = algorithmTag;
		this.problemTag = problem.getTag();
		this.referenceParetoFront = problem.getReferenceFront();
		this.dataset = dataset;
		this.runId = runId ;
	}

	public ExperimentAlgorithmMOCLE(
			Algorithm<Result> algorithm,
			ExperimentProblem<S> problem,
			Dataset dataset,
			int runId) {

		this(algorithm,algorithm.getName(),problem, dataset, runId);

	}

	public void runAlgorithm(ExperimentMOCLE<?, ?> experimentData) {
		String outputDirectoryName = experimentData.getExperimentBaseDirectory()
				+ "/data/"
				+ algorithmTag
				+ "/"
				+ problemTag;

		File outputDirectory = new File(outputDirectoryName);
		if (!outputDirectory.exists()) {
			boolean result = new File(outputDirectoryName).mkdirs();
			if (result) {
				JMetalLogger.logger.info("Creating " + outputDirectoryName);
			} else {
				JMetalLogger.logger.severe("Creating " + outputDirectoryName + " failed");
			}
		}

		String funFile = outputDirectoryName + "/FUN" + runId + ".tsv";
		String varFile = outputDirectoryName + "/VAR" + runId + ".tsv";
		String ariFile = outputDirectoryName + "/ARI" + runId + ".tsv";
		JMetalLogger.logger.info(
				" Running algorithm: " + algorithmTag +
						", problem: " + problemTag +
						", run: " + runId +
						", funFile: " + funFile);

		long initTime = System.currentTimeMillis();
		algorithm.run();
		long computingTime = System.currentTimeMillis() - initTime ;
		JMetalLogger.logger.info(
				" Running algorithm: " + algorithmTag +
						", problem: " + problemTag +
						", run: " + runId +
						", Total execution time: " + computingTime + "ms");
//		JMetalLogger.logger.log(Level.INFO, "Total execution time: {0}ms", computingTime);


		Result population = algorithm.getResult();

		Utils.removeRepeated((List<PartitionSolution>) population);

		Utils.computeAdjustedRand(dataset.getTruePartition(), (List<PartitionSolution>) population, new DefaultFileOutputContext(ariFile));

		new SolutionListOutput((List<S>) population)
				.setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext(varFile))
				.setFunFileOutputContext(new DefaultFileOutputContext(funFile))
				.print();
	}

	public Algorithm<Result> getAlgorithm() {
		return algorithm;
	}

	public String getAlgorithmTag() {
		return algorithmTag;
	}

	public String getProblemTag() {
		return problemTag;
	}

	public String getReferenceParetoFront() { return referenceParetoFront; }

	public int getRunId() { return this.runId;}
}
