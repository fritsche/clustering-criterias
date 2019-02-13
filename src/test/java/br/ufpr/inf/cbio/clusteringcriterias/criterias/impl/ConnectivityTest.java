/*
 * Copyright (C) 2019 Gian Fritsche <gmfritsche@inf.ufpr.br>
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
package br.ufpr.inf.cbio.clusteringcriterias.criterias.impl;

import br.ufpr.inf.cbio.clusteringcriterias.problem.DataSet;
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
 * @author Gian Fritsche <gmfritsche@inf.ufpr.br>
 */
public class ConnectivityTest {

    public ConnectivityTest() {
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
     * Test of computeDistanceMatrix method, of class Connectivity.
     */
    @Test
    public void testComputeDistanceMatrix() {
        System.out.println("computeDistanceMatrix");

        DataSet dataSet = new DataSet();

        dataSet.addDataPoint("a", new ArrayPoint(new double[]{0.0, 1.0}));
        dataSet.addDataPoint("b", new ArrayPoint(new double[]{0.0, 2.0}));
        dataSet.addDataPoint("c", new ArrayPoint(new double[]{0.0, 3.0}));

        PointDistance distance = new EuclideanDistance();
        Connectivity instance = new Connectivity(dataSet, distance);
        double[][] expResult = new double[][]{{0.0, 1.0, 2.0}, {1.0, 0.0, 1.0}, {2.0, 1.0, 0.0}};
        double[][] result = instance.computeDistanceMatrix(dataSet, distance);
        assertArrayEquals(expResult, result);
    }

}
