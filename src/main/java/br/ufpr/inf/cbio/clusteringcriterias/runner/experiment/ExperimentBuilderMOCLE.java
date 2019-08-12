package br.ufpr.inf.cbio.clusteringcriterias.runner.experiment;


import org.uma.jmetal.qualityindicator.impl.GenericIndicator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for class {@link Experiment}
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ExperimentBuilderMOCLE<S extends Solution<?>, Result extends List<S>> {
	private final String experimentName ;
	private List<ExperimentAlgorithmMOCLE<S, Result>> algorithmList;
	private List<ExperimentProblem<S>> problemList;
	private String referenceFrontDirectory;
	private String experimentBaseDirectory;
	private String outputParetoFrontFileName;
	private String outputAdjustedRandFileName;
	private String outputParetoSetFileName;
	private int independentRuns;

	private List<GenericIndicator<S>> indicatorList ;

	private int numberOfCores ;

	public ExperimentBuilderMOCLE(String experimentName) {
		this.experimentName = experimentName ;
		this.independentRuns = 1 ;
		this.numberOfCores = 1 ;
		this.referenceFrontDirectory = null ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.ExperimentBuilderMOCLE<S, Result> setAlgorithmList(List<ExperimentAlgorithmMOCLE<S, Result>> algorithmList) {
		this.algorithmList = new ArrayList<>(algorithmList) ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.ExperimentBuilderMOCLE<S, Result> setProblemList(List<ExperimentProblem<S>> problemList) {
		this.problemList = problemList ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.ExperimentBuilderMOCLE<S, Result> setExperimentBaseDirectory(String experimentBaseDirectory) {
		this.experimentBaseDirectory = experimentBaseDirectory+"/"+experimentName ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.ExperimentBuilderMOCLE<S, Result> setReferenceFrontDirectory(String referenceFrontDirectory) {
		this.referenceFrontDirectory = referenceFrontDirectory ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.ExperimentBuilderMOCLE<S, Result> setIndicatorList(
			List<GenericIndicator<S>> indicatorList ) {
		this.indicatorList = indicatorList ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.ExperimentBuilderMOCLE<S, Result> setOutputParetoFrontFileName(String outputParetoFrontFileName) {
		this.outputParetoFrontFileName = outputParetoFrontFileName ;

		return this ;
	}

	//used to print ARI files
	public br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.ExperimentBuilderMOCLE<S, Result> setOutputAdjustedRandFileName(String outputAdjustedRandFileName) {
		this.outputAdjustedRandFileName = outputAdjustedRandFileName ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.ExperimentBuilderMOCLE<S, Result> setOutputParetoSetFileName(String outputParetoSetFileName) {
		this.outputParetoSetFileName = outputParetoSetFileName ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.ExperimentBuilderMOCLE<S, Result> setIndependentRuns(int independentRuns) {
		this.independentRuns = independentRuns ;

		return this ;
	}

	public br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.ExperimentBuilderMOCLE<S, Result> setNumberOfCores(int numberOfCores) {
		this.numberOfCores = numberOfCores;

		return this ;
	}

	public ExperimentMOCLE<S, Result> build() {
		return new ExperimentMOCLE<S, Result>(this);
	}

	/* Getters */
	public String getExperimentName() {
		return experimentName;
	}

	public List<ExperimentAlgorithmMOCLE<S, Result>> getAlgorithmList() {
		return algorithmList;
	}

	public List<ExperimentProblem<S>> getProblemList() {
		return problemList;
	}

	public String getExperimentBaseDirectory() {
		return experimentBaseDirectory;
	}

	public String getOutputParetoFrontFileName() {
		return outputParetoFrontFileName;
	}

	public String getOutputAdjustedRandFileName() {
		return outputAdjustedRandFileName;
	}

	public String getOutputParetoSetFileName() {
		return outputParetoSetFileName;
	}

	public int getIndependentRuns() {
		return independentRuns;
	}

	public int getNumberOfCores() {
		return numberOfCores;
	}

	public String getReferenceFrontDirectory() {
		return referenceFrontDirectory;
	}

	public List<GenericIndicator<S>> getIndicatorList() {
		return indicatorList;
	}
}

