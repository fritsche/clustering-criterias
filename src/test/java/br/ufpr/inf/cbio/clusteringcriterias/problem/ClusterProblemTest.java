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
package br.ufpr.inf.cbio.clusteringcriterias.problem;

import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.ObjectiveFunction;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.Connectivity;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.OverallDeviation;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.DataPoint;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.DatasetFactory;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.point.impl.ArrayPoint;
import org.uma.jmetal.util.point.util.distance.EuclideanDistance;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class ClusterProblemTest {

    public ClusterProblemTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of evaluate method, of class ClusteringProblem.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");

        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test1.toString());

        dataset.setDataPoints(new ArrayList<DataPoint>(4));
        dataset.addDataPoint("a", new ArrayPoint(new double[]{1.0, 2.0}));
        dataset.addDataPoint("b", new ArrayPoint(new double[]{-1.0, 2.0}));
        dataset.addDataPoint("c", new ArrayPoint(new double[]{1.0, -1.0}));
        dataset.addDataPoint("d", new ArrayPoint(new double[]{-1.0, -1.0}));

        List<ObjectiveFunction> functions = new ArrayList<>(1);
        functions.add(new OverallDeviation(dataset, new EuclideanDistance()));
        functions.add(new Connectivity(Utils.computeNeighborhood(Utils.computeDistanceMatrix(dataset, new EuclideanDistance()), 0.5)));

        ClusterProblem problem = new ClusterProblem(true, dataset, functions);
        IntegerSolution s = problem.createSolution();

        s.setVariableValue(0, 0); // solution 'a' cluster 0
        s.setVariableValue(1, 0); // solution 'b' cluster 0

        s.setVariableValue(2, 1); // solution 'c' cluster 1
        s.setVariableValue(3, 1); // solution 'd' cluster 1

        problem.evaluate(s);

        double expected[] = new double[]{4.0, 2.0};
        double actual[] = s.getObjectives();
        assertTrue(Arrays.equals(expected, actual));
    }

    /**
     * Test of evaluate method, of class ClusteringProblem.
     */
    @Test
    public void testEvaluateOverallDeviationAndConnectivity() {
        System.out.println("evaluateOverallDeviationAndConnectivity");

        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test1.toString());
        dataset.setDataPoints(new ArrayList<DataPoint>(4));
        dataset.addDataPoint("a", new ArrayPoint(new double[]{1.0, 2.0}));
        dataset.addDataPoint("b", new ArrayPoint(new double[]{-1.0, 2.0}));
        dataset.addDataPoint("c", new ArrayPoint(new double[]{1.0, -1.0}));
        dataset.addDataPoint("d", new ArrayPoint(new double[]{-1.0, -1.0}));

        List<ObjectiveFunction> functions = new ArrayList<>(1);
        functions.add(new OverallDeviation(dataset, new EuclideanDistance()));

        List<List<Integer>> neighborhood = new ArrayList<>(4);
        neighborhood.add(new ArrayList<>(Arrays.asList(1)));
        neighborhood.add(new ArrayList<>(Arrays.asList(0)));
        neighborhood.add(new ArrayList<>(Arrays.asList(3)));
        neighborhood.add(new ArrayList<>(Arrays.asList(2)));
        functions.add(new Connectivity(neighborhood));

        ClusterProblem problem = new ClusterProblem(true, dataset, functions);
        IntegerSolution s = problem.createSolution();

        s.setVariableValue(0, 0); // solution 'a' cluster 0
        s.setVariableValue(1, 0); // solution 'b' cluster 0

        s.setVariableValue(2, 1); // solution 'c' cluster 1
        s.setVariableValue(3, 1); // solution 'd' cluster 1

        problem.evaluate(s);

        double[] expected = new double[]{4.0, 0.0};
        double[] actual = s.getObjectives();
        assertTrue(Arrays.equals(expected, actual));
    }

    /**
     * Test of createSolution method, of class ClusterProblem.
     */
    @Test
    public void testCreateSolution() {
        System.out.println("createSolution");

        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test1.toString());

        ClusterProblem instance = new ClusterProblem(true, dataset, new ArrayList<ObjectiveFunction>());

        IntegerSolution a = instance.createSolution();
        int[] resultA = new int[5];
        int[] expResultA = new int[]{1, 0, 1, 0, 2};
        for (int i = 0; i < a.getNumberOfVariables(); i++) {
            resultA[i] = a.getVariableValue(i);
        }

        IntegerSolution b = instance.createSolution();
        int[] resultB = new int[5];
        int[] expResultB = new int[]{1, 0, 0, 1, 2};
        for (int i = 0; i < b.getNumberOfVariables(); i++) {
            resultB[i] = b.getVariableValue(i);
        }

        Assert.assertArrayEquals(expResultA, resultA);
        Assert.assertArrayEquals(expResultB, resultB);
    }

}
