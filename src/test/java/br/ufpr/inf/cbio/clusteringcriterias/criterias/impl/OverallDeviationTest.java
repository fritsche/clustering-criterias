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
package br.ufpr.inf.cbio.clusteringcriterias.criterias.impl;

import br.ufpr.inf.cbio.clusteringcriterias.problem.ClusterProblem;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.DatasetFactory;
import br.ufpr.inf.cbio.clusteringcriterias.problem.PartitionCentroids;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.point.impl.ArrayPoint;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class OverallDeviationTest {

    public OverallDeviationTest() {
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
     * Test of evaluate method, of class OverallDeviation.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");

        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test1.toString());
        dataset.setDataPoints(new ArrayList<>(4));
        dataset.addDataPoint("a", new ArrayPoint(new double[]{1.0, 1.0}));
        dataset.addDataPoint("b", new ArrayPoint(new double[]{-1.0, 1.0}));
        dataset.addDataPoint("c", new ArrayPoint(new double[]{1.0, -1.0}));
        dataset.addDataPoint("d", new ArrayPoint(new double[]{-1.0, -1.0}));

        ClusterProblem problem = new ClusterProblem(false, dataset, new ArrayList<>());
        IntegerSolution s = problem.createSolution();

        s.setVariableValue(0, 0); // solution 'a' cluster 0
        s.setVariableValue(1, 0); // solution 'b' cluster 0

        s.setVariableValue(2, 1); // solution 'c' cluster 1
        s.setVariableValue(3, 1); // solution 'd' cluster 1

        PartitionCentroids partitionCentroids = new PartitionCentroids();
        partitionCentroids.computeCentroids(s, dataset);

        OverallDeviation instance = new OverallDeviation(dataset);
        double expResult = 4.0;
        double result = instance.evaluate(s);
        System.out.println("Evaluate Overall Deviation, expected: " + expResult + ", actual: " + result);
        assertEquals(expResult, result, 10e-6);
    }

}
