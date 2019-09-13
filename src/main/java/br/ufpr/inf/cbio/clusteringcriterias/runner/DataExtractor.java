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
import br.ufpr.inf.cbio.clusteringcriterias.problem.ClusterProblem;
import br.ufpr.inf.cbio.clusteringcriterias.problem.Utils;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import br.ufpr.inf.cbio.hhco.util.output.OutputWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jep.JepException;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class DataExtractor {

    public static void dataExtractor(String datasetName, OutputWriter ow) throws JepException {
        System.out.println(datasetName);
        Dataset dataset = DatasetFactory.getInstance().getDataset(datasetName);
        int[] truePartition = dataset.getTruePartition();
        
        List<ObjectiveFunction> functions = new ArrayList<>();
        functions.add(new Connectivity(Utils.computeNeighborhood(dataset.getDistanceMatrix())));
        functions.add(new DaviesBouldin(dataset));
        functions.add(new InvertedMinimumCentroidDistance(dataset));
        functions.add(new MaximumDiameter(dataset));
        functions.add(new MinimizationSeparation(dataset));
        functions.add(new MinimizationSilhouette(dataset));
        functions.add(new OverallDeviation(dataset));
        
        ClusterProblem problem = new ClusterProblem(true, dataset, functions);
        PartitionSolution trueSolution = problem.createSolution();
        Set set = new HashSet<Integer>();
        for (int i = 0; i < trueSolution.getNumberOfVariables() - 1; i++) {
            trueSolution.setVariableValue(i, truePartition[i]);
            set.add(truePartition[i]);
        }
        trueSolution.setVariableValue(trueSolution.getNumberOfVariables() - 1, set.size());
        problem.evaluate(trueSolution);
        double[] trueObjectives = trueSolution.getObjectives();

        int[] trueSolutionIsBetter = new int[functions.size()];
        int[] trueSolutionIsWorse = new int[functions.size()];
        int[] trueSolutionIsEqual = new int[functions.size()];
        int trueSolutionDominates = 0;
        int trueSolutionIsDominated = 0;
        int trueSolutionIsNonDominated = 0;

        int popSize = problem.getPopulationSize();
        for (int i = 0; i < popSize; i++) {
            PartitionSolution solution = problem.createSolution();
            problem.evaluate(solution);
            double[] objectives = solution.getObjectives();
            int countWorse = 0;
            int countBetter = 0;
            for (int j = 0; j < objectives.length; j++) {
                if (trueObjectives[j] < objectives[j]) {
                    countBetter++;
                    trueSolutionIsBetter[j]++;
                } else if (trueObjectives[j] > objectives[j]) {
                    countWorse++;
                    trueSolutionIsWorse[j]++;
                } else {
                    trueSolutionIsEqual[j]++;
                }
            }
            if (countWorse == 0) {
                trueSolutionDominates++;
            } else if (countBetter == 0) {
                trueSolutionIsDominated++;
            } else {
                trueSolutionIsNonDominated++;
            }
        }

        String better = "";
        better += datasetName + ", ";
        better += "better, ";
        for (int i = 0; i < trueObjectives.length; i++) {
            better += trueSolutionIsBetter[i] + ", ";
        }
        better += "dominates, ";
        better += trueSolutionDominates;
        ow.writeLine(better);

        String worse = "";
        worse += datasetName + ", ";
        worse += "worse, ";
        for (int i = 0; i < trueObjectives.length; i++) {
            worse += trueSolutionIsWorse[i] + ", ";
        }
        worse += "dominated, ";
        worse += trueSolutionIsDominated;
        ow.writeLine(worse);

        String equal = "";
        equal += datasetName + ", ";
        equal += "equal, ";
        for (int i = 0; i < trueObjectives.length; i++) {
            equal += trueSolutionIsEqual[i] + ", ";
        }
        equal += "nondominated, ";
        equal += trueSolutionIsNonDominated;
        ow.writeLine(equal);

    }

    public static void main(String[] args) throws JepException {
        OutputWriter ow = new OutputWriter("experiment/", "metricsanalysis.csv");
        dataExtractor(DatasetFactory.DATASET.D31.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.ds2c2sc13_V1.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.ds2c2sc13_V2.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.ds2c2sc13_V3.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.ds3c3sc6_V1.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.ds3c3sc6_V2.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.spiralsquare.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.tevc_20_60_1.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.frogs_V1.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.frogs_V2.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.frogs_V3.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.golub_V1.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.golub_V3.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.leukemia_V1.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.leukemia_V2.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.libras.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.optdigits.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.seeds.toString(), ow);
        dataExtractor(DatasetFactory.DATASET.UKC1.toString(), ow);
        ow.close();
    }
}
