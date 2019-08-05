package br.ufpr.inf.cbio.clusteringcriterias.runner.experiment;

import br.ufpr.inf.cbio.clusteringcriterias.criterias.ObjectiveFunction;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.Connectivity;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.OverallDeviation;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.DatasetFactory;
import br.ufpr.inf.cbio.clusteringcriterias.operator.HBGFCrossover;
import br.ufpr.inf.cbio.clusteringcriterias.problem.ClusterProblem;
import br.ufpr.inf.cbio.clusteringcriterias.problem.Utils;
import br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.ExperimentAlgorithmMOCLE;
import br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.ExperimentBuilderMOCLE;
import br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.ExperimentMOCLE;
import br.ufpr.inf.cbio.clusteringcriterias.runner.experiment.componets.ExecuteAlgorithmsMOCLE;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import cern.colt.matrix.DoubleMatrix2D;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.mutation.NullMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
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

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      throw new JMetalException("Needed arguments: experimentBaseDirectory");
    }
    String experimentBaseDirectory = args[0];
    String referenceParetoFront = "";

    Problem problem;
    Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.ds2c2sc13_V1.toString());
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
            configureAlgorithmList(problemList);

    ExperimentMOCLE<PartitionSolution, List<PartitionSolution>> experiment =
            new ExperimentBuilderMOCLE<PartitionSolution, List<PartitionSolution>>("Experiment0")
                    .setAlgorithmList(algorithmList)
                    .setProblemList(problemList)
                    .setExperimentBaseDirectory(experimentBaseDirectory)
                    .setOutputParetoFrontFileName("FUN")
                    .setOutputParetoSetFileName("VAR")
                    .setReferenceFrontDirectory(experimentBaseDirectory + "/Experiment0/referenceFronts")
                    .setIndicatorList(Arrays.asList(
                            new PISAHypervolume<PartitionSolution>(referenceParetoFront)))
                    .setIndependentRuns(INDEPENDENT_RUNS)
                    .build();

    new ExecuteAlgorithmsMOCLE<>(experiment).run();

//    new GenerateReferenceParetoFront(experiment).run();
//    necessário para calcular os indicadores
//    new GenerateFronts(experiment).run();

//    new ComputeQualityIndicatorsMOCLE<>(experiment).run();

  }

  static List<ExperimentAlgorithmMOCLE<PartitionSolution, List<PartitionSolution>>> configureAlgorithmList(
          List<ExperimentProblem<PartitionSolution>> problemList) throws IOException {
    List<ExperimentAlgorithmMOCLE<PartitionSolution, List<PartitionSolution>>> algorithms = new ArrayList<>();

    double crossoverProbability;
    CrossoverOperator<PartitionSolution> crossover;
    MutationOperator<PartitionSolution> mutation;

    //verify neighborhood % on problem.utils
    crossoverProbability = 1.0;
    int numberOfGeneratedChild = 2;
    crossover = new HBGFCrossover(crossoverProbability, numberOfGeneratedChild);
    mutation = new NullMutation<>();
    SelectionOperator<List<PartitionSolution>, PartitionSolution> selection;
    selection = new BinaryTournamentSelection<>(
            new RankingAndCrowdingDistanceComparator<>());


    for (int run = 0; run < INDEPENDENT_RUNS; run++) {

//      for (int i = 0; i < problemList.size(); i++){
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxIterations = 50;
//
//        Algorithm<List<PartitionSolution>> algorithm = new NSGAIIIBuilder<>(problemList.get(i).getProblem())
//                .setCrossoverOperator(crossover)
//                .setMutationOperator(mutation)
//                .setSelectionOperator(selection)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setMaxIterations(maxIterations)
//                .build();
//        algorithms.add(new ExperimentAlgorithm<>(algorithm,"NSGAIIIa",problemList.get(i), run));
//
//      }

//
//      for (int i = 0; i < problemList.size(); i++){
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxIterations = 50;
//
//        new GenerateWeightVector(popSize).run();
//
//        Algorithm<List<PartitionSolution>> algorithm = new MOMBI2<>(problem,maxIterations,crossover,mutation,selection,new SequentialSolutionListEvaluator<PartitionSolution>(),
//                "weight_vectors/a.sld");
//        algorithms.add(new ExperimentAlgorithm<>(algorithm,"MOMBI2a",problemList.get(i), run));
//
//      }
//
//      for (int i = 0; i < problemList.size(); i++) {
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxFitnessEvaluations = popSize * 50;
//
//        Algorithm<List<PartitionSolution>> algorithm = new SPEA2Builder<>(problemList.get(i).getProblem(),crossover,mutation)
//                .setSelectionOperator(selection)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setMaxIterations(50)
//                .build();
//        algorithms.add(new ExperimentAlgorithm<>(algorithm,"SPEA2a",problemList.get(i),run));
//      }
//
//      for (int i = 0; i < problemList.size(); i++){
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxFitnessEvaluations = popSize * 50;
//
//                Algorithm<List<PartitionSolution>> algorithm = new CLUMOEADBuilder(problem, CLUMOEADBuilder.Variant.MOEAD)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setMaxEvaluations(50)
//                .setCrossover(crossover)
//                .setMutation(mutation)
//                .build();
//        algorithms.add(new ExperimentAlgorithm<>(algorithm,"MOEADa",problemList.get(i),run));
//
//      }
//
//      for (int i = 0; i < problemList.size(); i++){
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxFitnessEvaluations = popSize * 50;
//
//        Algorithm<List<PartitionSolution>> algorithm = new CLUMOEADBuilder(problem, CLUMOEADBuilder.Variant.MOEADD)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setMaxEvaluations(50)
//                .setCrossover(crossover)
//                .setMutation(mutation)
//                .build();
//        algorithms.add(new ExperimentAlgorithm<>(algorithm,"MOEADDa",problemList.get(i),run));
//
//      }
//
//      for (int i = 0; i < problemList.size(); i++){
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxIterations = 50;
//
//        Algorithm<List<PartitionSolution>> algorithm = new CLUSPEA2SDEBuilder<>(problemList.get(i).getProblem())
//                .setCrossoverOperator(crossover)
//                .setMutationOperator(mutation)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setMaxIterations(maxIterations)
//                .setSelectionOperator(selection)
//                .build();
//        algorithms.add(new ExperimentAlgorithm<>(algorithm,"SPEA2SDEa",problemList.get(i),run));
//      }
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
//                .setMaxGenerations(50)
//                .build();
//        algorithms.add(new ExperimentAlgorithm<>(algorithm,"ThetaDEAa",problemList.get(i),run));
//      }
//
      for (int i = 0; i < problemList.size(); i++) {

        Problem problem = problemList.get(i).getProblem();
        int popSize = ((ClusterProblem) problem).getPopulationSize();
        int maxFitnessEvaluations = popSize * 50;

        Algorithm<List<PartitionSolution>> algorithm = new NSGAIIBuilder<>(problemList.get(i).getProblem(), crossover, mutation)
                .setSelectionOperator(selection)
                .setMaxEvaluations(maxFitnessEvaluations)
                .setPopulationSize(popSize + (popSize % 2))
                .build();
        algorithms.add(new ExperimentAlgorithmMOCLE<>(algorithm, "NSGAIIa", problemList.get(i), run));

      }
//
//      for (int i = 0; i < problemList.size(); i++) {
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxFitnessEvaluations = popSize * 50;
//
//        Algorithm<List<PartitionSolution>> algorithm = new CLUIBEABuilder(problem)
//                .setMaxEvaluations(maxFitnessEvaluations)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setSelection(selection)
//                .build();
//        algorithms.add(new ExperimentAlgorithm<>(algorithm, "IBEAa", problemList.get(i), run));
//      }
//
//      for (int i = 0; i < problemList.size(); i++) {
//
//        Problem problem = problemList.get(i).getProblem();
//        int popSize = ((ClusterProblem) problem).getPopulationSize();
//        int maxFitnessEvaluations = popSize * 50;
//
//        Algorithm<List<PartitionSolution>> algorithm = new HypEBuilder<>(problem)
//                .setCrossoverOperator(crossover)
//                .setMaxEvaluations(maxFitnessEvaluations)
//                .setMutationOperator(mutation)
//                .setPopulationSize(popSize + (popSize % 2))
//                .setSelectionOperator(selection)
//                .build();
//        algorithms.add(new ExperimentAlgorithm<>(algorithm, "HypEa", problemList.get(i), run));
//
//      }
//

    }

    return algorithms;
  }

}
