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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
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

        dataset.setDataPoints(new ArrayList<>(3));
        dataset.addDataPoint("a", new ArrayPoint(new double[]{0.0, 1.0}));
        dataset.addDataPoint("b", new ArrayPoint(new double[]{0.0, 2.0}));
        dataset.addDataPoint("c", new ArrayPoint(new double[]{0.0, 3.0}));

        PointDistance distance = new EuclideanDistance();
        double[][] expResult = new double[][]{{0.0, 1.0, 2.0}, {1.0, 0.0, 1.0}, {2.0, 1.0, 0.0}};

        DoubleMatrix2D res = Utils.computeDistanceMatrix(dataset, distance);

        double[][] result = res.toArray();

        assertArrayEquals(expResult, result);
    }

    /**
     * Test of computeNeighborhood method, of class Utils.
     */
    @Test
    public void testComputeNeighborhood() {
        System.out.println("computeNeighborhood");

        double[][] dist = new double[][]{
            {0.0, 2.0, 3.0, 4.0},
            {8.0, 0.0, 6.0, 5.0},
            {14.0, 9.0, 0.0, 11.0},
            {15.0, 13.0, 10.0, 0.0}
        };

        DoubleMatrix2D distances = new DenseDoubleMatrix2D(dist);

        List<List<Integer>> expResult = new ArrayList<>(4);
        expResult.add(new ArrayList<>(Arrays.asList(1, 2)));
        expResult.add(new ArrayList<>(Arrays.asList(3, 2)));
        expResult.add(new ArrayList<>(Arrays.asList(1, 3)));
        expResult.add(new ArrayList<>(Arrays.asList(2, 1)));

        List<List<Integer>> result = Utils.computeNeighborhood(distances, 2);
        System.out.println(result);

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

    @Test
    public void testgenerateWeightVector() throws FileNotFoundException {
        System.out.println("generateWeightVector");
        double[][] expResult = new double[][]{
            {1.0, 0.0},
            {0.8888888888888888, 0.11111111111111116},
            {0.7777777777777777, 0.22222222222222232},
            {0.6666666666666665, 0.3333333333333335},
            {0.5555555555555554, 0.44444444444444464},
            {0.44444444444444425, 0.5555555555555558},
            {0.33333333333333315, 0.6666666666666669},
            {0.22222222222222204, 0.7777777777777779},
            {0.11111111111111094, 0.8888888888888891},
            {-1.6653345369377348E-16, 1.0000000000000002}};

        Utils.generateWeightVector(10);

        Scanner input = new Scanner(new FileInputStream(getClass().getClassLoader().getResource("weight_vectors/a.sld").getFile()));
        
        int rows = 10;
        int columns = 2;
        double[][] result = new double[rows][columns];
        input.nextLine();
        while (input.hasNextLine()) {
            for (int i = 0; i < result.length; i++) {
                String[] line = input.nextLine().trim().split(" ");
                for (int j = 0; j < line.length; j++) {
                    result[i][j] = Double.parseDouble(line[j]);
                }
            }
        }
        System.out.println(Arrays.deepToString(result));

        Assert.assertArrayEquals(expResult, result);
    }


}
