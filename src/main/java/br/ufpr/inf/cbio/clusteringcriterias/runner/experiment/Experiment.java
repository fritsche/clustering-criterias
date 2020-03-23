package br.ufpr.inf.cbio.clusteringcriterias.runner.experiment;

import br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.CLUIBEABuilder;
import br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.MOEAD.AbstractMOEAD;
import br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.MOEAD.CLUMOEAD;
import br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.MOEAD.CLUMOEADBuilder;
import br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.HypE.HypEBuilder;
import br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.MOMBI2.CLUMOMBI2;
import br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.NSGAIII.CLUNSGAIIIBuilder;
import br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.SPEA2SDE.CLUSPEA2SDEBuilder;
import br.ufpr.inf.cbio.clusteringcriterias.algorithm.builders.thetaDEA.ThetaDEABuilder;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.ObjectiveFunction;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.Connectivity;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.OverallDeviation;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.DatasetFactory;
import br.ufpr.inf.cbio.clusteringcriterias.operator.*;
import br.ufpr.inf.cbio.clusteringcriterias.problem.ClusterProblem;
import br.ufpr.inf.cbio.clusteringcriterias.problem.Utils;
import br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.componets.ComputeQualityIndicatorsMOCLE;
import br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.componets.ExecuteAlgorithmsMOCLE;
import br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.componets.GenerateFronts;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import cern.colt.matrix.DoubleMatrix2D;
import jep.JepException;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEAD;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2Builder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.mutation.NullMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;
import org.uma.jmetal.util.point.util.distance.EuclideanDistance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example of experimental study based on solving the Cluster problems with NSGA-II
 * <p>
 * This experiment assumes that the reference Pareto front are not known, so the names of files
 * containing them and the directory where they are located must be specified.
 * <p>
 * Six quality indicators are used for performance assessment.
 * <p>
 * The steps to carry out the experiment are: 1. Configure the experiment 2. Execute the algorithms
 * 3. Generate the reference Pareto fronts 4. Compute the quality indicators 5. Generate Latex
 * tables reporting means and medians 6. Generate Latex tables with the result of applying the
 * Wilcoxon Rank Sum Test 7. Generate Latex tables with the ranking obtained by applying the
 * Friedman test 8. Generate R scripts to obtain boxplots
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class Experiment {

  private static final int INDEPENDENT_RUNS = 30;


  public void execute(String datasetName, String experimentBaseDirectory) throws IOException, JepException {

    String referenceParetoFront = "";
    System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); //used for TimSort bug on HypE and(or) MOMBI running parallel -> (Experiment) -> https://stackoverflow.com/questions/13575224/comparison-method-violates-its-general-contract-timsort-and-gridlayout

    Problem problem;
    Dataset dataset = DatasetFactory.getInstance().getDataset(datasetName);
//    referenceParetoFront = NSGAII.class.getClassLoader().getResource("pareto_fronts/ZDT1.pf").getFile();

    List<ObjectiveFunction> functions = new ArrayList<>();
    functions.add(new OverallDeviation(dataset, new EuclideanDistance()));
    DoubleMatrix2D distanceMatrix = Utils.computeDistanceMatrix(dataset, new EuclideanDistance());
    List<List<Integer>> neighborhood = Utils.computeNeighborhood(distanceMatrix);
    functions.add(new Connectivity(neighborhood));

    problem = new ClusterProblem(true, dataset, functions);

    List<ExperimentProblem<PartitionSolution>> problemList = new ArrayList<>();
    problemList.add(new ExperimentProblem<>(problem));

    List<ExperimentAlgorithmMOCLE<PartitionSolution, List<PartitionSolution>>> algorithmList =
            configureAlgorithmList(problemList,dataset); //included dataset to calc Adjusted Rand

    ExperimentMOCLE<PartitionSolution, List<PartitionSolution>> experiment =
            new ExperimentBuilderMOCLE<PartitionSolution, List<PartitionSolution>>("Experiment0")
                    .setAlgorithmList(algorithmList)
                    .setProblemList(problemList)
                    .setExperimentBaseDirectory(experimentBaseDirectory)
                    .setOutputParetoFrontFileName("FUN")
                    .setOutputParetoSetFileName("VAR")
                    .setOutputAdjustedRandFileName("ARI")
                    .setReferenceFrontDirectory(experimentBaseDirectory + "/Experiment0/referenceFronts")
                    .setIndicatorList(Arrays.asList(
                            new PISAHypervolume<>()))
                    .setNumberOfCores(1) //todo: check concurrentHash
                    .setIndependentRuns(INDEPENDENT_RUNS)
                    .build();

    new ExecuteAlgorithmsMOCLE<>(experiment).run();

    new GenerateFronts(experiment).run();

    new ComputeQualityIndicatorsMOCLE<>(experiment).run();

  }

  static List<ExperimentAlgorithmMOCLE<PartitionSolution, List<PartitionSolution>>> configureAlgorithmList(
          List<ExperimentProblem<PartitionSolution>> problemList, Dataset dataset) throws IOException, JepException {
    List<ExperimentAlgorithmMOCLE<PartitionSolution, List<PartitionSolution>>> algorithms = new ArrayList<>();

    CrossoverOperator<PartitionSolution> crossover;
    MutationOperator<PartitionSolution> mutation;

//    crossover = new HBGFCrossover(20);
    crossover = new HBGFCrossover();
//    crossover = new MixedHBGFCrossover(20);
//    crossover = new MCLACrossover();
//    crossover = new CECrossover();
//    crossover = new CSPACrossover(dataset);
//    mutation = new ClusterRandomMutation(0.5);
    mutation = new NullMutation<>();
//    mutation = new CentroidBasedMutation(dataset);

    SelectionOperator<List<PartitionSolution>, PartitionSolution> selection;
    selection = new BinaryTournamentSelection<>(
            new RankingAndCrowdingDistanceComparator<>());

    Problem problem0 = problemList.get(0).getProblem();
    Utils.generateWeightVector(((ClusterProblem) problem0).getPopulationSize()+(((ClusterProblem) problem0).getPopulationSize()%2));


    for (int run = 0; run < 30; run++) {


//      for (int i = 0; i < problemList.size(); i++){
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxIterations = 51;
//

//        Algorithm<List<PartitionSolution>> algorithm = new CLUMOMBI2<>(problem,maxIterations,crossover,mutation,selection, new SequentialSolutionListEvaluator<>(),
//                "weight_vectors/a.sld");
//        algorithms.add(new ExperimentAlgorithmMOCLE<>(algorithm,"MOMBI2",problemList.get(i), dataset, run));
//
//      }
//
//       for (int i = 0; i < problemList.size(); i++) {
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxFitnessEvaluations = popSize * 51;
//
//        Algorithm<List<PartitionSolution>> algorithm = new HypEBuilder<>(problem)
//                .setCrossoverOperator(crossover)
//                .setMaxEvaluations(maxFitnessEvaluations)
//                .setMutationOperator(mutation)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setSelectionOperator(selection)
//                .build();
//        algorithms.add(new ExperimentAlgorithmMOCLE<>(algorithm, "HypE", problemList.get(i), dataset,run));
//
//      }
//
      for (int i = 0; i < problemList.size(); i++) {

        Problem problem = problemList.get(i).getProblem();
        int popSize = ((ClusterProblem) problem).getPopulationSize();
        int maxFitnessEvaluations = popSize * 51;

        Algorithm<List<PartitionSolution>> algorithm = new NSGAIIBuilder<>(problemList.get(i).getProblem(), crossover, mutation, popSize + (popSize % 2))
                .setSelectionOperator(selection)
                .setOffspringPopulationSize(((popSize + (popSize % 2))/2))
                .setMaxEvaluations(maxFitnessEvaluations)
                .build();
        algorithms.add(new ExperimentAlgorithmMOCLE<>(algorithm, "NSGAII", problemList.get(i), dataset, run));

      }

//      for (int i = 0; i < problemList.size(); i++){
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxEvaluations = popSize * 51;
//
//        Algorithm<List<PartitionSolution>> algorithm = new CLUNSGAIIIBuilder<>(problemList.get(i).getProblem(), crossover, mutation)
//                .setSelection(selection)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setMaxEvaluations(maxEvaluations)
//                .build();
//        algorithms.add(new ExperimentAlgorithmMOCLE<>(algorithm,"NSGAIII",problemList.get(i), dataset, run));
//
//      }
//
//
//      for (int i = 0; i < problemList.size(); i++){
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//
//        Algorithm<List<PartitionSolution>> algorithm = new ThetaDEABuilder<>(problem)
//                .setCrossover(crossover)
//                .setMutation(mutation)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setMaxGenerations(51)
//                .build();
//        algorithms.add(new ExperimentAlgorithmMOCLE<>(algorithm,"ThetaDEA",problemList.get(i),dataset,run));
//      }
//
//      for (int i = 0; i < problemList.size(); i++) {
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//
//        Algorithm<List<PartitionSolution>> algorithm = new SPEA2Builder<>(problemList.get(i).getProblem(),crossover,mutation)
//                .setSelectionOperator(selection)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setMaxIterations(51)
//                .build();
//        algorithms.add(new ExperimentAlgorithmMOCLE<>(algorithm,"SPEA2",problemList.get(i),dataset,run));
//      }
//
//
//
//      for (int i = 0; i < problemList.size(); i++){
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxIterations = 51;
//
//        Algorithm<List<PartitionSolution>> algorithm = new CLUSPEA2SDEBuilder<>(problemList.get(i).getProblem())
//                .setCrossoverOperator(crossover)
//                .setMutationOperator(mutation)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setMaxIterations(maxIterations)
//                .setSelectionOperator(selection)
//                .build();
//        algorithms.add(new ExperimentAlgorithmMOCLE<>(algorithm,"SPEA2SDE",problemList.get(i),dataset,run));
//      }
//
//      for (int i = 0; i < problemList.size(); i++) {
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxFitnessEvaluations = popSize * 51;
//
//        Algorithm<List<PartitionSolution>> algorithm = new CLUIBEABuilder(problem)
//                .setMaxEvaluations(maxFitnessEvaluations)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setSelection(selection)
//                .build();
//        algorithms.add(new ExperimentAlgorithmMOCLE<>(algorithm, "IBEA", problemList.get(i),dataset,run));
//      }
//
//      for (int i = 0; i < problemList.size(); i++){
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxFitnessEvaluations = popSize * 51;
//
//                Algorithm<List<PartitionSolution>> algorithm = new CLUMOEADBuilder(problem, CLUMOEADBuilder.Variant.MOEADSTM)
//                        .setPopulationSize(popSize + (popSize % 2))
//                        .setMaxEvaluations(maxFitnessEvaluations)
//                        .setCrossover(crossover)
//                        .setMutation(mutation)
//                        .setNeighborSize(2)
//                        .setFunctionType(CLUMOEAD.FunctionType.AGG)
//                        .setMaximumNumberOfReplacedSolutions(1)
//                        .setNeighborhoodSelectionProbability(1)
//                        .setResultPopulationSize(popSize + (popSize % 2))
//                        .build();
//        algorithms.add(new ExperimentAlgorithmMOCLE<>(algorithm,"MOEADSTM",problemList.get(i),dataset,run));
//
//      }
//
//      for (int i = 0; i < problemList.size(); i++){
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxFitnessEvaluations = popSize * 51;
//
//        Algorithm<List<PartitionSolution>> algorithm = new CLUMOEADBuilder(problem, CLUMOEADBuilder.Variant.MOEAD)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setMaxEvaluations(maxFitnessEvaluations)
//                .setCrossover(crossover)
//                .setMutation(mutation)
//                .setNeighborSize(2)
//                .setFunctionType(CLUMOEAD.FunctionType.AGG)
//                .setMaximumNumberOfReplacedSolutions(1)
//                .setNeighborhoodSelectionProbability(1)
//                .setResultPopulationSize(popSize + (popSize % 2))
//                .build();
//        algorithms.add(new ExperimentAlgorithmMOCLE<>(algorithm,"MOEAD",problemList.get(i),dataset,run));
//
//      }
//
//      for (int i = 0; i < problemList.size(); i++){
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxFitnessEvaluations = popSize * 51;
//
//        Algorithm<List<PartitionSolution>> algorithm = new CLUMOEADBuilder(problem, CLUMOEADBuilder.Variant.MOEADD)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setMaxEvaluations(maxFitnessEvaluations)
//                .setCrossover(crossover)
//                .setMutation(mutation)
//                .setNeighborSize(Math.toIntExact(Math.round((popSize * 0.2))))
//                .setResultPopulationSize(popSize + (popSize % 2))
//                .build();
//        algorithms.add(new ExperimentAlgorithmMOCLE<>(algorithm,"MOEADD",problemList.get(i),dataset,run));
//
//      }

    }

    return algorithms;
  }

  public static void main(String[] args) throws IOException, JepException {
    if (args.length != 1) {
      throw new JMetalException("Needed arguments: experimentBaseDirectory");
    }

//    (new Experiment()).execute("D31", args[0]);
    (new Experiment()).execute("ds2c2sc13_V2", args[0]);
//    (new Experiment()).execute("ds2c2sc13_V2", args[0]);
//    (new Experiment()).execute("ds2c2sc13_V3", args[0]);
//    (new Experiment()).execute("ds3c3sc6_V1", args[0]);
//    (new Experiment()).execute("ds3c3sc6_V2", args[0]);
//    (new Experiment()).execute("frogs_V1", args[
//    0]);
//    (new Experiment()).execute("frogs_V2", args[0]);
//    (new Experiment()).execute("frogs_V3", args[0]);
//    (new Experiment()).execute("golub_V1", args[0]);
//    (new Experiment()).execute("golub_V3", args[0]);
//    (new Experiment()).execute("leukemia_V1", args[0]);
//    (new Experiment()).execute("leukemia_V2", args[0]);
//    (new Experiment()).execute("libras", args[0]);
//    (new Experiment()).execute("optdigits", args[0]);
//    (new Experiment()).execute("seeds", args[0]);
//    (new Experiment()).execute("spiralsquare", args[0]);
//    (new Experiment()).execute("tevc_20_60_1", args[0]); //too slow
//    (new Experiment()).execute("UKC1", args[0]);
  }

}
