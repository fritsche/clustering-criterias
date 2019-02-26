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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class PartitionCentroidsTest {

    public PartitionCentroidsTest() {
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
     * Test of computeCentroids method, of class PartitionCentroids.
     */
    @Test
    public void testComputeCentroidsWithNegativeValuesAndTwoClusters() {
        System.out.println("computeCentroidsWithNegativeValuesAndTwoClusters");
        DataSet dataSet = new DataSet();

        dataSet.addDataPoint("a", new ArrayPoint(new double[]{1.0, 1.0}));
        dataSet.addDataPoint("b", new ArrayPoint(new double[]{-1.0, 1.0}));
        dataSet.addDataPoint("c", new ArrayPoint(new double[]{1.0, -1.0}));
        dataSet.addDataPoint("d", new ArrayPoint(new double[]{-1.0, -1.0}));

        int maxK = 2;
        ClusterProblem problem = new ClusterProblem(false, dataSet, new ArrayList<ObjectiveFunction>(), maxK);
        IntegerSolution s = problem.createSolution();

        s.setVariableValue(0, 0); // solution 'a' cluster 0
        s.setVariableValue(1, 0); // solution 'b' cluster 0

        s.setVariableValue(2, 1); // solution 'c' cluster 1
        s.setVariableValue(3, 1); // solution 'd' cluster 1

        PartitionCentroids instance = new PartitionCentroids();
        instance.computeCentroids(s, dataSet);

        Map<Integer, Point> centroids = instance.getAttribute(s);

        System.out.println("Cluster 0 centroid expected [0.0, 1.0]: actual " + Arrays.toString(centroids.get(0).getValues()));
        assertTrue(Arrays.equals(new double[]{0.0, 1.0}, centroids.get(0).getValues()));
        System.out.println("Cluster 1 centroid expected [0.0, -1.0]: actual " + Arrays.toString(centroids.get(1).getValues()));
        assertTrue(Arrays.equals(new double[]{0.0, -1.0}, centroids.get(1).getValues()));

    }

    @Test
    public void testComputeCentroids() {
        System.out.println("computeCentroids");
        DataSet dataSet = new DataSet();

        dataSet.addDataPoint("a", new ArrayPoint(new double[]{1.0, 1.0}));
        dataSet.addDataPoint("b", new ArrayPoint(new double[]{2.0, 2.0}));
        dataSet.addDataPoint("c", new ArrayPoint(new double[]{0.0, 2.0}));

        int maxK = 1;
        ClusterProblem problem = new ClusterProblem(false, dataSet, new ArrayList<ObjectiveFunction>(), maxK);
        IntegerSolution s = problem.createSolution();

        s.setVariableValue(0, 0); // solution 'a' cluster 0
        s.setVariableValue(1, 0); // solution 'b' cluster 0
        s.setVariableValue(2, 0); // solution 'c' cluster 0

        PartitionCentroids instance = new PartitionCentroids();
        instance.computeCentroids(s, dataSet);

        Map<Integer, Point> centroids = instance.getAttribute(s);

        System.out.println("Cluster 0 centroid expected [1.0, 1.6666666666666667]: actual " + Arrays.toString(centroids.get(0).getValues()));
        assertTrue(Arrays.equals(new double[]{3.0 / 3.0, 5.0 / 3.0}, centroids.get(0).getValues()));

    }

}
