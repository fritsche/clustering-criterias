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

import br.ufpr.inf.cbio.clusteringcriterias.criterias.ObjectiveFunction;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.Connectivity;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.OverallDeviation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
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

        DataSet dataSet = new DataSet();

        dataSet.addDataPoint("a", new ArrayPoint(new double[]{1.0, 1.0}));
        dataSet.addDataPoint("b", new ArrayPoint(new double[]{-1.0, 1.0}));
        dataSet.addDataPoint("c", new ArrayPoint(new double[]{1.0, -1.0}));
        dataSet.addDataPoint("d", new ArrayPoint(new double[]{-1.0, -1.0}));

        List<ObjectiveFunction> functions = new ArrayList<>(1);
        functions.add(new OverallDeviation(dataSet, new EuclideanDistance()));

        ClusterProblem problem = new ClusterProblem(true, dataSet, functions, Utils.getInitialPartitionFiles("clustering/test/initialPartitions"));
        IntegerSolution s = problem.createSolution();

        s.setVariableValue(0, 0); // solution 'a' cluster 0
        s.setVariableValue(1, 0); // solution 'b' cluster 0

        s.setVariableValue(2, 1); // solution 'c' cluster 1
        s.setVariableValue(3, 1); // solution 'd' cluster 1

        problem.evaluate(s);

        double expected = 4.0;
        double actual = s.getObjective(0);
        System.out.println("Evaluate Clustering Problem using Overall Deviation, expected: " + expected + ", actual: " + actual);
        assertEquals(expected, actual, 0.0);
    }

    /**
     * Test of evaluate method, of class ClusteringProblem.
     */
    @Test
    public void testEvaluateOverallDeviationAndConnectivity() {
        System.out.println("evaluateOverallDeviationAndConnectivity");

        DataSet dataSet = new DataSet();

        dataSet.addDataPoint("a", new ArrayPoint(new double[]{1.0, 2.0}));
        dataSet.addDataPoint("b", new ArrayPoint(new double[]{-1.0, 2.0}));
        dataSet.addDataPoint("c", new ArrayPoint(new double[]{1.0, -1.0}));
        dataSet.addDataPoint("d", new ArrayPoint(new double[]{-1.0, -1.0}));

        List<ObjectiveFunction> functions = new ArrayList<>(1);
        functions.add(new OverallDeviation(dataSet, new EuclideanDistance()));

        List<List<Integer>> neighborhood = new ArrayList<>(4);
        neighborhood.add(new ArrayList<>(Arrays.asList(1)));
        neighborhood.add(new ArrayList<>(Arrays.asList(0)));
        neighborhood.add(new ArrayList<>(Arrays.asList(3)));
        neighborhood.add(new ArrayList<>(Arrays.asList(2)));
        functions.add(new Connectivity(neighborhood));

        ClusterProblem problem = new ClusterProblem(true, dataSet, functions, Utils.getInitialPartitionFiles("clustering/test/initialPartitions"));
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

}
