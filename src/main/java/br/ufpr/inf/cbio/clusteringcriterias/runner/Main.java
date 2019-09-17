/*
 * Copyright (C) 2019 Gian Fritsche <gmfritsche at inf.ufpr.br>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.ufpr.inf.cbio.clusteringcriterias.runner;

import br.ufpr.inf.cbio.clusteringcriterias.algorithm.SPEA2SDE.SPEA2SDEConfiguration;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.ObjectiveFunction;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.Connectivity;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.DaviesBouldin;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.InvertedMinimumCentroidDistance;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.MaximumDiameter;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.MinimizationSeparation;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.MinimizationSilhouette;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.OverallDeviation;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.DatasetFactory;
import br.ufpr.inf.cbio.clusteringcriterias.operator.HBGFCrossover;
import br.ufpr.inf.cbio.clusteringcriterias.problem.ClusterProblem;
import br.ufpr.inf.cbio.clusteringcriterias.problem.Utils;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import static br.ufpr.inf.cbio.hhco.runner.Main.help;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jep.JepException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.mutation.NullMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class Main {

    private long seed;
    private String experimentBaseDirectory;
    private int id;

    public static <S extends Solution<?>> boolean contains(List<S> population, Solution s1) {

        if (population == null || s1 == null) {
            throw new IllegalArgumentException("The list of solutions or the solution cannot be null");
        }

        for (int i = 0; i < population.size(); i++) {

            Solution s2 = population.get(i);

            if (isEqual(s1, s2)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isEqual(Solution s1, Solution s2) {

        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("Soluton s1 and s2 cannot be null");
        }

        if (s1.getNumberOfObjectives() != s2.getNumberOfObjectives()) {
            throw new IllegalArgumentException("Solutions cannot be different number of objetives");
        }

        for (int i = 0; i < s1.getNumberOfObjectives(); i++) {

            if (s1.getObjective(i) != s2.getObjective(i)) {
                return false;
            }
        }

        return true;
    }

    public static <S extends Solution<?>> List<S> removeRepeatedSolutions(List<S> solutions) {

        List<S> newPopulation = new ArrayList<>();

        for (int i = 0; i < solutions.size(); i++) {

            S s = solutions.get(i);

            if (!contains(newPopulation, s)) {
                newPopulation.add((S) s.copy());
            }
        }

        return newPopulation;
    }

    public static CommandLine parse(String[] args) {

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        Options options = new Options();

        try {
            options.addOption(Option.builder("h").longOpt("help").desc("print this message and exits.").build());
            options.addOption(Option.builder("P").longOpt("output-path").hasArg().argName("path")
                    .desc("directory path for output (if no path is given experiment/ will be used.)").build());
            options.addOption(Option.builder("id").hasArg().argName("id")
                    .desc("set the independent run id, default 0.").build());
            options.addOption(Option.builder("n").hasArg().argName("norm")
                    .desc("normalize dataset [true, false].").build());
            options.addOption(Option.builder("s").longOpt("seed").hasArg().argName("seed")
                    .desc("set the seed for JMetalRandom, default System.currentTimeMillis()").build());
            options.addOption(Option.builder("a").longOpt("algorithm").hasArg().argName("algorithm")
                    .desc("set the algorithm to be executed (default SPEA2SDE).").build());
            options.addOption(Option.builder("p").longOpt("problem").hasArg().argName("problem")
                    .desc("set the problem instance.").build());
            options.addOption(Option.builder("m").longOpt("objectives").hasArg().argName("objectives")
                    .desc("set the number of objectives [2, 4a, 4b, 7]").build());

            // parse command line
            cmd = parser.parse(options, args);
            // print help and exit
            if (cmd.hasOption("h") || args.length == 0) {
                help(options);
                System.exit(0);
            }
            return cmd;
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
                    "Failed to parse command line arguments. Execute with -h for usage help.", ex);
        }
        return null;
    }

    public static void help(Options options) {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                "java -cp <jar> " + Main.class.getName(),
                "\nExecute a single independent run of the <algorithm> on a given <problem>.\n",
                options,
                "\nPlease report any issues.", true);
    }

    public Algorithm configure(CommandLine cmd) throws JepException {
        String problemName = "D31", aux;
        String m = "2";
        seed = System.currentTimeMillis();
        experimentBaseDirectory = "experiment/";
        id = 0;
        String algorithmName = "SPEA2SDE";
        boolean normalize = true;

        if ((aux = cmd.getOptionValue("a")) != null) {
            algorithmName = aux;
        }
        if ((aux = cmd.getOptionValue("p")) != null) {
            problemName = aux;
        }
        if ((aux = cmd.getOptionValue("P")) != null) {
            experimentBaseDirectory = aux;
        }
        if ((aux = cmd.getOptionValue("m")) != null) {
            m = aux;
        }
        if ((aux = cmd.getOptionValue("id")) != null) {
            id = Integer.parseInt(aux);
        }
        if ((aux = cmd.getOptionValue("s")) != null) {
            seed = Long.parseLong(aux);
        }
        if ((aux = cmd.getOptionValue("n")) != null) {
            normalize = Boolean.parseBoolean(aux);
        }

        JMetalRandom.getInstance().setSeed(seed);
        Dataset dataset;
        if (normalize) {
            dataset = DatasetFactory.getInstance().getNormalizedDataset(problemName);
        } else {
            dataset = DatasetFactory.getInstance().getDataset(problemName);
        }
        List<ObjectiveFunction> functions = new ArrayList<>();
        switch (m) {
            case "2":
                functions.add(new Connectivity(Utils.computeNeighborhood(dataset.getDistanceMatrix())));
                functions.add(new OverallDeviation(dataset));
                break;
            case "4a":
                functions.add(new Connectivity(Utils.computeNeighborhood(dataset.getDistanceMatrix())));
                functions.add(new MinimizationSilhouette(dataset));
                functions.add(new InvertedMinimumCentroidDistance(dataset));
                functions.add(new OverallDeviation(dataset));
                break;
            case "4b":
                functions.add(new Connectivity(Utils.computeNeighborhood(dataset.getDistanceMatrix())));
                functions.add(new MinimizationSilhouette(dataset));
                functions.add(new InvertedMinimumCentroidDistance(dataset));
                functions.add(new MaximumDiameter(dataset));
                break;
            case "7":
                functions.add(new Connectivity(Utils.computeNeighborhood(dataset.getDistanceMatrix())));
                functions.add(new MinimizationSilhouette(dataset));
                functions.add(new InvertedMinimumCentroidDistance(dataset));
                functions.add(new OverallDeviation(dataset));
                functions.add(new DaviesBouldin(dataset));
                functions.add(new MinimizationSeparation(dataset));
                functions.add(new MaximumDiameter(dataset));
                break;
            default:
                throw new JMetalException("There is no configuration for " + m + " objectives.");
        }

        ClusterProblem problem = new ClusterProblem(true, dataset, functions);

        CrossoverOperator<PartitionSolution> crossover;
        MutationOperator<PartitionSolution> mutation;
        crossover = new HBGFCrossover();
        mutation = new NullMutation<>();
        SelectionOperator<List<PartitionSolution>, PartitionSolution> selection;
        selection = new BinaryTournamentSelection<>();
        int popSize = ((ClusterProblem) problem).getPopulationSize();
        int iterations = 51;
        
        if (algorithmName.equals("SPEA2SDE")) {
            return new SPEA2SDEConfiguration().configure(problem, crossover, mutation, popSize, selection, iterations);
        } else {
            throw new JMetalException("There is no configuration for " + algorithmName + " algorithm.");
        }
    }

    public <S extends Solution<?>> void printResult(List<S> solutions) {
        List population = removeRepeatedSolutions(SolutionListUtils.getNondominatedSolutions(solutions));
        String folder = experimentBaseDirectory + "/";
        br.ufpr.inf.cbio.hhco.util.output.Utils outputUtils = new br.ufpr.inf.cbio.hhco.util.output.Utils(folder);
        outputUtils.prepareOutputDirectory();
        new SolutionListOutput(population).setSeparator("\t")
                .setVarFileOutputContext(new DefaultFileOutputContext(folder + "VAR" + id + ".tsv"))
                .setFunFileOutputContext(new DefaultFileOutputContext(folder + "FUN" + id + ".tsv"))
                .print();
    }

    public static void main(String[] args) throws JepException {
        Main main = new Main();
        Algorithm<List<PartitionSolution>> algorithm = main.configure(parse(args));
        algorithm.run();
        main.printResult(algorithm.getResult());
    }

}
