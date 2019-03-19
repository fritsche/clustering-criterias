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
import br.ufpr.inf.cbio.clusteringcriterias.dataset.DataSet;
import java.io.File;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.uma.jmetal.util.point.impl.ArrayPoint;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class DataSetTest {

    public DataSetTest() {
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
     * Test of parseFile method, of class DataSet.
     */
    @Test
    public void testParseFile() {
        System.out.println("parseFile");
        File file = new File(getClass().getClassLoader().getResource("datasets/test/test1/dataset.txt").getFile());
        DataSet dataSet = new DataSet();
        dataSet.addDataPoint("a", new ArrayPoint(new double[]{1.0, 1.0}));
        dataSet.addDataPoint("b", new ArrayPoint(new double[]{-1.0, 1.0}));
        dataSet.addDataPoint("c", new ArrayPoint(new double[]{1.0, -1.0}));
        dataSet.addDataPoint("d", new ArrayPoint(new double[]{-1.0, -1.0}));

        List<DataPoint> expResult = dataSet.getDataPoints();
        List<DataPoint> result = DataSet.parseFile(file);
        System.out.println("Expected: ");
        System.out.println(expResult);
        System.out.println("Result: ");
        System.out.println(result);
        assertEquals(expResult, result);
    }

}
