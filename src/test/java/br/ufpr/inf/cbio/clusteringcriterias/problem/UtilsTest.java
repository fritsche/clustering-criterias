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

import br.ufpr.inf.cbio.clusteringcriterias.dataset.DataPoint;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.DatasetFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.uma.jmetal.util.point.impl.ArrayPoint;
import org.uma.jmetal.util.point.util.distance.EuclideanDistance;
import org.uma.jmetal.util.point.util.distance.PointDistance;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class UtilsTest {

    public UtilsTest() {
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
     * Test of computeDistanceMatrix method, of class Utils.
     */
    @Test
    public void testComputeDistanceMatrix() {
        System.out.println("computeDistanceMatrix");
        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test1.toString());

        dataset.setDataPoints(new ArrayList<DataPoint>(3));
        dataset.addDataPoint("a", new ArrayPoint(new double[]{0.0, 1.0}));
        dataset.addDataPoint("b", new ArrayPoint(new double[]{0.0, 2.0}));
        dataset.addDataPoint("c", new ArrayPoint(new double[]{0.0, 3.0}));

        PointDistance distance = new EuclideanDistance();
        double[][] expResult = new double[][]{{0.0, 1.0, 2.0}, {1.0, 0.0, 1.0}, {2.0, 1.0, 0.0}};
        double[][] result = Utils.computeDistanceMatrix(dataset, distance);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of computeNeighborhood method, of class Utils.
     */
    @Test
    public void testComputeNeighborhood() {
        System.out.println("computeNeighborhood");

        double[][] distances = new double[][]{
            {0.0, 2.0, 3.0, 4.0},
            {8.0, 0.0, 6.0, 5.0},
            {14.0, 9.0, 0.0, 11.0},
            {15.0, 13.0, 10.0, 0.0}
        };

        List<List<Integer>> expResult = new ArrayList<>(4);
        expResult.add(new ArrayList<>(Arrays.asList(1, 2)));
        expResult.add(new ArrayList<>(Arrays.asList(3, 2)));
        expResult.add(new ArrayList<>(Arrays.asList(1, 3)));
        expResult.add(new ArrayList<>(Arrays.asList(2, 1)));

        List<List<Integer>> result = Utils.computeNeighborhood(distances, 2);
        assertEquals(expResult, result);
    }

    /**
     * Test of getNeighbors method, of class Utils.
     */
    @Test
    public void testGetNeighbors() {
        System.out.println("getNeighbors");
        List<Double> di = new ArrayList<>(Arrays.asList(4.0, 3.0, 2.0, 1.0));
        int i = 2;
        int k = 2;
        List<Integer> expResult = Arrays.asList(3, 1);
        List<Integer> result = Utils.getNeighbors(di, i, k);
        assertEquals(expResult, result);
    }

    /**
     * Test of getNeighbors method, of class Utils.
     */
    @Test
    public void testGetNeighborsAgain() {
        System.out.println("getNeighborsAgain");
        List<Double> di = new ArrayList<>(Arrays.asList(40.0, 28.0, 0.0, 10.0, 5.0, 3.0, 1.0));
        int i = 2;
        int k = 3;
        List<Integer> expResult = Arrays.asList(6, 5, 4);
        List<Integer> result = Utils.getNeighbors(di, i, k);
        assertEquals(expResult, result);
    }

        /**
     * Test of getNeighbors method, of class Utils.
     */
    @Test
    public void testGetNeighborsWithRepeatedDistances() {
        System.out.println("getNeighborsWithRepeatedDistances");
        List<Double> di = new ArrayList<>(Arrays.asList(1.0, 40.0, 0.0, 28.0, 1.0, 20.0, 1.0, 10.0, 1.0, 5.0, 1.0, 3.0, 1.0));
        int i = 2;
        int k = 3;
        List<Integer> expResult = Arrays.asList(0, 4, 6);
        List<Integer> result = Utils.getNeighbors(di, i, k);
        assertEquals(expResult, result);
    }

}
