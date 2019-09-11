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

import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.DatasetFactory;
import br.ufpr.inf.cbio.clusteringcriterias.problem.ClusterProblem;
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
public class InvertedMinimumCentroidDistanceTest {
    
    public InvertedMinimumCentroidDistanceTest() {
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
     * Test of evaluate method, of class InvertedMinimumCentroidDistance.
     */
@Test
    public void testEvaluate() {
        System.out.println("evaluate");

        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test3.toString());
        
        ClusterProblem problem = new ClusterProblem(false, dataset, new ArrayList<>());
        IntegerSolution s = problem.createSolution();

        s.setVariableValue(0, 0);
        s.setVariableValue(1, 0);
        s.setVariableValue(2, 0);
        s.setVariableValue(3, 1);
        s.setVariableValue(4, 2);
        s.setVariableValue(5, 2);
        s.setVariableValue(6, 1);
        s.setVariableValue(7, 2);
        s.setVariableValue(8, 2);
        s.setVariableValue(9, 3);

        PartitionCentroids partitionCentroids = new PartitionCentroids();
        partitionCentroids.computeCentroids(s, dataset);

        InvertedMinimumCentroidDistance instance = new InvertedMinimumCentroidDistance(dataset);
        double expResult = 0.469664;
        double result = instance.evaluate(s);
        System.out.println("Inverted Minimum Centroid Distance expected: " + expResult + ", actual: " + result);
        assertEquals(expResult, result, 10e-6);
    }

    
}
