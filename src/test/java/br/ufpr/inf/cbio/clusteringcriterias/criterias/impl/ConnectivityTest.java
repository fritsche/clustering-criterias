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

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
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
     * Test of evaluate method, of class Connectivity.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        
        // four points dataset (point values don't matter on this test)
        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test1.toString());
        dataset.setDataPoints(new ArrayList<>(4));
        dataset.addDataPoint("a", new ArrayPoint());
        dataset.addDataPoint("b", new ArrayPoint());
        dataset.addDataPoint("c", new ArrayPoint());
        dataset.addDataPoint("d", new ArrayPoint());
        
        ClusterProblem problem = new ClusterProblem(false, dataset, new ArrayList<>());
        
        IntegerSolution s = problem.createSolution();
        s.setVariableValue(0, 0);
        s.setVariableValue(1, 0);
        s.setVariableValue(2, 1);
        s.setVariableValue(3, 1);

        List<List<Integer>> neighborhood = new ArrayList<>(4);
        neighborhood.add(new ArrayList<>(Arrays.asList(1, 3)));
        neighborhood.add(new ArrayList<>(Arrays.asList(0, 2)));
        neighborhood.add(new ArrayList<>(Arrays.asList(3, 1)));
        neighborhood.add(new ArrayList<>(Arrays.asList(2, 0)));

        Connectivity instance = new Connectivity(neighborhood);

        double expResult = 2.0;
        double result = instance.evaluate(s);
        assertEquals(expResult, result, 0.0);
    }

}
