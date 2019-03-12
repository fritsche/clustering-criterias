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
package br.ufpr.inf.cbio.clusteringcriterias.operator;

import br.ufpr.inf.cbio.clusteringcriterias.operator.util.GraphCSR;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class HBGFCrossoverTest {

    private int nvtxs;
    private int[] xadj;
    private int[] adjncy;
    private List<Integer> a, b;

    public HBGFCrossoverTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        nvtxs = 11;
        xadj = new int[]{0, 2, 4, 6, 8, 10, 12, 14, 17, 21, 25, 28};
        adjncy = new int[]{
            7, 9, // 0
            7, 9, // 1
            8, 9, // 2
            8, 10, // 3
            8, 10, // 4
            8, 10, // 5
            7, 9, // 6
            0, 1, 6,
            2, 3, 4, 5,
            0, 1, 2, 6,
            3, 4, 5
        };
        a = new ArrayList<>();
        a.add(0);
        a.add(0);
        a.add(1);
        a.add(1);
        a.add(1);
        a.add(1);
        a.add(0);
        a.add(2);
        b = new ArrayList<>();
        b.add(0);
        b.add(0);
        b.add(0);
        b.add(1);
        b.add(1);
        b.add(1);
        b.add(0);
        b.add(2);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getPartition method, of class HBGFCrossover.
     */
    @Test
    public void testGetPartition() {
        System.out.println("getPartition");
        HBGFCrossover instance = new HBGFCrossover();
        HBGFCrossover.Partition result = instance.getPartition();
        assertNotNull(result);
    }

    /**
     * Test of partition method, of class HBGFCrossover.
     */
    @Test
    public void testPartition() {
        System.out.println("partition");
        int nparts = 2;
        int[] part = new int[nvtxs];
        HBGFCrossover instance = new HBGFCrossover();
        int[] expPart = new int[]{1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0};
        instance.partition(nvtxs, xadj, adjncy, nparts, part);
        Assert.assertArrayEquals(expPart, part);
    }

    /**
     * Test of convertToGraph method, of class HBGFCrossover.
     */
    @Test
    public void testConvertToGraph() {
        System.out.println("convertToGraph");
        HBGFCrossover instance = new HBGFCrossover();
        GraphCSR expResult = new GraphCSR(nvtxs, xadj, adjncy);
        GraphCSR result = instance.convertToGraph(a, b);
        assertEquals(expResult, result);
    }

    /**
     * Test of hbgf method, of class HBGFCrossover.
     */
    @Test
    public void testHbgf() {
        System.out.println("hbgf");
        HBGFCrossover instance = new HBGFCrossover();
        List<Integer> expResult = new ArrayList<>();
        expResult.add(1);
        expResult.add(1);
        expResult.add(0);
        expResult.add(0);
        expResult.add(0);
        expResult.add(0);
        expResult.add(1);
        expResult.add(2);
        List<Integer> result = instance.hbgf(a, b);
        assertEquals(expResult, result);
    }

}
