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

import br.ufpr.inf.cbio.clusteringcriterias.criterias.ObjectiveFunction;
import br.ufpr.inf.cbio.clusteringcriterias.operator.util.GraphCSR;
import br.ufpr.inf.cbio.clusteringcriterias.problem.ClusterProblem;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.DatasetFactory;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import br.ufpr.inf.cbio.clusteringcriterias.utils.MockRandomNumberGenerator;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class HBGFCrossoverTest {

    private int nvtxs;
    private int[] xadj;
    private int[] adjncy;
    private PartitionSolution a, b, c;

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

        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test2.toString());
        ClusterProblem problem = new ClusterProblem(false, dataset, new ArrayList<ObjectiveFunction>());
        a = (PartitionSolution) problem.createSolution();
        b = (PartitionSolution) problem.createSolution();
        c = (PartitionSolution) a.copy();
        c.setVariableValue(0, 1);
        c.setVariableValue(1, 1);
        c.setVariableValue(2, 0);
        c.setVariableValue(3, 0);
        c.setVariableValue(4, 0);
        c.setVariableValue(5, 0);
        c.setVariableValue(6, 1);
        c.setVariableValue(7, 2);

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
    public void testPartitionWithDifferentNumberOfClusters() {
        System.out.println("partitionWithDifferentNumberOfClusters");
        int nparts = 3;
        int nvtxs_ = 15;
        int[] xadj_ = new int[]{0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 23, 27, 29, 32, 34, 36};
        int[] adjncy_ = new int[]{9, 11, 9, 12, 9, 12, 9, 11, 9, 12, 10, 13, 10, 14, 10, 14, 10, 13, 0, 1, 2, 3, 4, 5, 6, 7, 8, 0, 3, 1, 2, 4, 5, 8, 6, 7};
        int[] part = new int[nvtxs_];
        HBGFCrossover instance = new HBGFCrossover();
        int[] expPart = new int[]{2, 1, 1, 2, 1, 0, 0, 0, 2, 1, 0, 2, 1, 2, 0};
        instance.partition(nvtxs_, xadj_, adjncy_, nparts, part);
        Assert.assertArrayEquals(expPart, part);
    }

    /**
     * Test of convertToGraph method, of class HBGFCrossover.
     */
    @Test
    public void testConvertToGraph2() {
        System.out.println("convertToGraph2");
        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test4.toString());
        ClusterProblem problem = new ClusterProblem(false, dataset, new ArrayList<ObjectiveFunction>());
        PartitionSolution x = (PartitionSolution) problem.createSolution();
        PartitionSolution y = (PartitionSolution) problem.createSolution();
        int nvtxs_ = 15;
        int[] xadj_ = new int[]{0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 23, 27, 29, 32, 34, 36};
        int[] adjncy_ = new int[]{9, 11, 9, 12, 9, 12, 9, 11, 9, 12, 10, 13, 10, 14, 10, 14, 10, 13, 0, 1, 2, 3, 4, 5, 6, 7, 8, 0, 3, 1, 2, 4, 5, 8, 6, 7};
        HBGFCrossover instance = new HBGFCrossover();
        GraphCSR expResult = new GraphCSR(nvtxs_, xadj_, adjncy_);
        GraphCSR result = instance.convertToGraph(x, y);
        assertEquals(expResult, result);
    }

    /**
     * Test of convertToGraph method, of class HBGFCrossover.
     */
    @Test
    public void testConvertToGraph4() {
        System.out.println("convertToGraph4");
        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test5.toString());
        ClusterProblem problem = new ClusterProblem(false, dataset, new ArrayList<ObjectiveFunction>());
        PartitionSolution x = (PartitionSolution) problem.createSolution();
        PartitionSolution y = (PartitionSolution) problem.createSolution();
        int nvtxs_ = 596;
        int[] xadj_ = new int[]{0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74, 76, 78, 80, 82, 84, 86, 88, 90, 92, 94, 96, 98, 100, 102, 104, 106, 108, 110, 112, 114, 116, 118, 120, 122, 124, 126, 128, 130, 132, 134, 136, 138, 140, 142, 144, 146, 148, 150, 152, 154, 156, 158, 160, 162, 164, 166, 168, 170, 172, 174, 176, 178, 180, 182, 184, 186, 188, 190, 192, 194, 196, 198, 200, 202, 204, 206, 208, 210, 212, 214, 216, 218, 220, 222, 224, 226, 228, 230, 232, 234, 236, 238, 240, 242, 244, 246, 248, 250, 252, 254, 256, 258, 260, 262, 264, 266, 268, 270, 272, 274, 276, 278, 280, 282, 284, 286, 288, 290, 292, 294, 296, 298, 300, 302, 304, 306, 308, 310, 312, 314, 316, 318, 320, 322, 324, 326, 328, 330, 332, 334, 336, 338, 340, 342, 344, 346, 348, 350, 352, 354, 356, 358, 360, 362, 364, 366, 368, 370, 372, 374, 376, 378, 380, 382, 384, 386, 388, 390, 392, 394, 396, 398, 400, 402, 404, 406, 408, 410, 412, 414, 416, 418, 420, 422, 424, 426, 428, 430, 432, 434, 436, 438, 440, 442, 444, 446, 448, 450, 452, 454, 456, 458, 460, 462, 464, 466, 468, 470, 472, 474, 476, 478, 480, 482, 484, 486, 488, 490, 492, 494, 496, 498, 500, 502, 504, 506, 508, 510, 512, 514, 516, 518, 520, 522, 524, 526, 528, 530, 532, 534, 536, 538, 540, 542, 544, 546, 548, 550, 552, 554, 556, 558, 560, 562, 564, 566, 568, 570, 572, 574, 576, 578, 580, 582, 584, 586, 588, 590, 592, 594, 596, 598, 600, 602, 604, 606, 608, 610, 612, 614, 616, 618, 620, 622, 624, 626, 628, 630, 632, 634, 636, 638, 640, 642, 644, 646, 648, 650, 652, 654, 656, 658, 660, 662, 664, 666, 668, 670, 672, 674, 676, 678, 680, 682, 684, 686, 688, 690, 692, 694, 696, 698, 700, 702, 704, 706, 708, 710, 712, 714, 716, 718, 720, 722, 724, 726, 728, 730, 732, 734, 736, 738, 740, 742, 744, 746, 748, 750, 752, 754, 756, 758, 760, 762, 764, 766, 768, 770, 772, 774, 776, 778, 780, 782, 784, 786, 788, 790, 792, 794, 796, 798, 800, 802, 804, 806, 808, 810, 812, 814, 816, 818, 820, 822, 824, 826, 828, 830, 832, 834, 836, 838, 840, 842, 844, 846, 848, 850, 852, 854, 856, 858, 860, 862, 864, 866, 868, 870, 872, 874, 876, 878, 880, 882, 884, 886, 888, 890, 892, 894, 896, 898, 900, 902, 904, 906, 908, 910, 912, 914, 916, 918, 920, 922, 924, 926, 928, 930, 932, 934, 936, 938, 940, 942, 944, 946, 948, 950, 952, 954, 956, 958, 960, 962, 964, 966, 968, 970, 972, 974, 976, 978, 980, 982, 984, 986, 988, 990, 992, 994, 996, 998, 1000, 1002, 1004, 1006, 1008, 1010, 1012, 1014, 1016, 1018, 1020, 1022, 1024, 1026, 1028, 1030, 1032, 1034, 1036, 1038, 1040, 1042, 1044, 1046, 1048, 1050, 1052, 1054, 1056, 1058, 1060, 1062, 1064, 1066, 1068, 1070, 1072, 1074, 1076, 1078, 1080, 1082, 1084, 1086, 1088, 1090, 1092, 1094, 1096, 1098, 1100, 1102, 1104, 1106, 1108, 1110, 1112, 1114, 1116, 1118, 1120, 1122, 1124, 1126, 1128, 1130, 1132, 1134, 1136, 1138, 1140, 1142, 1144, 1146, 1148, 1150, 1152, 1154, 1156, 1158, 1160, 1162, 1164, 1166, 1168, 1170, 1172, 1174, 1176, 1408, 1522, 1764, 1872, 1996, 2110, 2202, 2352};
        int[] adjncy_ = new int[]{589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 592, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 589, 593, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 590, 594, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 595, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596, 591, 596,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232,
            233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342, 343, 344, 345, 346,
            347, 348, 349, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 360, 361, 362, 363, 364, 365, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 396, 397, 398, 399, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 425, 426, 427, 428, 429, 430, 431, 432, 433, 434, 435, 436, 437, 438, 439, 440, 441, 442, 443, 444, 445, 446, 447, 448, 449, 450, 451, 452, 453, 454, 455, 456, 457, 458, 459, 460, 461, 462, 463, 464, 465, 466, 467, 468, 469, 470, 471, 472, 473, 474, 475, 476, 477, 478, 479, 480, 481, 482, 483, 484, 485, 486, 487, 488, 489, 490, 491, 492, 493, 494, 495, 496, 497, 498, 499, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511, 512, 513, 514, 515, 516, 517, 518, 519, 520, 521, 522, 523, 524, 525, 526, 527, 528, 529, 530, 531, 532, 533, 534, 535, 536, 537, 538, 539, 540, 541, 542, 543, 544, 545, 546, 547, 548, 549, 550, 551, 552, 553, 554, 555, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566, 567, 568, 569, 570, 571, 572, 573, 574, 575, 576, 577, 578, 579, 580, 581, 582, 583, 584, 585, 586, 587, 588,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108,
            109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232,
            233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342, 343, 344, 345, 346,
            347, 348, 349, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 360, 361, 362, 363, 364, 365, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 396, 397, 398, 399, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 425, 426, 427, 428, 429, 430, 431, 432, 433, 434, 435, 436, 437, 438,
            439, 440, 441, 442, 443, 444, 445, 446, 447, 448, 449, 450, 451, 452, 453, 454, 455, 456, 457, 458, 459, 460, 461, 462, 463, 464, 465, 466, 467, 468, 469, 470, 471, 472, 473, 474, 475, 476, 477, 478, 479, 480, 481, 482, 483, 484, 485, 486, 487, 488, 489, 490, 491, 492, 493, 494, 495, 496, 497, 498, 499, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511, 512, 513, 514, 515, 516, 517, 518, 519, 520, 521, 522, 523, 524, 525, 526, 527, 528, 529, 530, 531, 532, 533, 534, 535, 536, 537, 538, 539, 540, 541, 542, 543, 544, 545, 546, 547, 548, 549, 550, 551, 552, 553, 554, 555, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566, 567, 568, 569, 570, 571, 572, 573, 574, 575, 576, 577, 578, 579, 580, 581, 582, 583, 584, 585, 586, 587, 588
        };
        for (int i = 0; i < adjncy_.length; i++) {
            adjncy_[i] = adjncy_[i] - 1;
        }
        GraphCSR expResult = new GraphCSR(nvtxs_, xadj_, adjncy_);

        HBGFCrossover instance = new HBGFCrossover();
        GraphCSR result = instance.convertToGraph(x, y);
        System.out.println(expResult);
        System.out.println(result);
        assertEquals(expResult, result);
    }

    /**
     * Test of partition method, of class HBGFCrossover.
     */
    @Test
    public void testPartitionVersusMOCLE() {
        System.out.println("partitionVersusMOCLE");
        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test5.toString());
        ClusterProblem problem = new ClusterProblem(false, dataset, new ArrayList<ObjectiveFunction>());
        PartitionSolution x = (PartitionSolution) problem.createSolution();
        PartitionSolution y = (PartitionSolution) problem.createSolution();
        int nvtxs_ = 596;
        int[] part = new int[nvtxs_];
        HBGFCrossover instance = new HBGFCrossover();
        GraphCSR gcsr = instance.convertToGraph(x, y);
        int[] expPart = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 2, 1, 1, 2, 2, 1, 2, 1, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 2, 2, 0, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 0, 1, 1, 2, 0, 0};
        instance.partition(nvtxs_, gcsr.getAdjacencyIndexes(), gcsr.getAdacencies(), 3, part);
        Assert.assertArrayEquals(expPart, part);
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
        PartitionSolution result = instance.hbgf(a, b);
        assertEquals(c, result);
    }

    /**
     * Test of getNumberOfRequiredParents method, of class HBGFCrossover.
     */
    @Test
    public void testGetNumberOfRequiredParents() {
        System.out.println("getNumberOfRequiredParents");
        HBGFCrossover instance = new HBGFCrossover();
        int expResult = 2; // default is two
        int result = instance.getNumberOfRequiredParents();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNumberOfGeneratedChildren method, of class HBGFCrossover.
     */
    @Test
    public void testGetNumberOfGeneratedChildren() {
        System.out.println("getNumberOfGeneratedChildren");
        HBGFCrossover instance = new HBGFCrossover();
        int expResult = 2; // default is two
        int result = instance.getNumberOfGeneratedChildren();
        assertEquals(expResult, result);
    }

    /**
     * Test of execute method, of class HBGFCrossover.
     */
    @Test
    public void testExecute() {
        System.out.println("execute");
        List<PartitionSolution> source = new ArrayList<>();
        source.add(a);
        source.add(b);
        HBGFCrossover instance = new HBGFCrossover();
        List<PartitionSolution> expResult = new ArrayList<>();
        expResult.add(c);
        expResult.add(c);
        List<PartitionSolution> result = instance.execute(source);
        assertEquals(expResult, result);
    }

    /**
     * Test of doCrossover method, of class HBGFCrossover.
     */
    @Test
    public void testDoCrossover() {
        System.out.println("doCrossover");
        double probability = 1.0;
        HBGFCrossover instance = new HBGFCrossover();
        List<PartitionSolution> expResult = new ArrayList<>();
        expResult.add(c);
        expResult.add(c);
        List<PartitionSolution> result = instance.doCrossover(probability, a, b);
        assertEquals(expResult, result);
    }

    /**
     * Another test of execute method, of class HBGFCrossover.
     */
    @Test
    public void testExecuteScenario2() {
        System.out.println("executeScenario2");
        JMetalRandom.getInstance().setRandomGenerator(new MockRandomNumberGenerator(new double[]{0.0, 3, 3}));
        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test3.toString());
        ClusterProblem problem = new ClusterProblem(false, dataset, new ArrayList<ObjectiveFunction>());
        PartitionSolution x = (PartitionSolution) problem.createSolution();
        PartitionSolution y = (PartitionSolution) problem.createSolution();
        PartitionSolution z = (PartitionSolution) x.copy();
        z.setVariableValue(0, 0);
        z.setVariableValue(1, 0);
        z.setVariableValue(2, 0);
        z.setVariableValue(3, 1);
        z.setVariableValue(4, 1);
        z.setVariableValue(5, 1);
        z.setVariableValue(6, 2);
        z.setVariableValue(7, 2);
        z.setVariableValue(8, 2);
        z.setVariableValue(9, 3);

        List<PartitionSolution> source = new ArrayList<>();
        source.add(x);
        source.add(y);
        HBGFCrossover instance = new HBGFCrossover();
        List<PartitionSolution> expResult = new ArrayList<>();
        expResult.add(z);
        expResult.add(z);
        List<PartitionSolution> result = instance.execute(source);
        assertEquals(expResult, result);
    }

    /**
     * Another test of execute method, of class HBGFCrossover.
     */
    @Test
    public void testExecuteScenario3() {
        System.out.println("executeScenario3");
        JMetalRandom.getInstance().setRandomGenerator(new MockRandomNumberGenerator(new double[]{0.0, 3, 3}));
        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test4.toString());
        ClusterProblem problem = new ClusterProblem(false, dataset, new ArrayList<ObjectiveFunction>());
        PartitionSolution x = (PartitionSolution) problem.createSolution();
        PartitionSolution y = (PartitionSolution) problem.createSolution();
        PartitionSolution z = (PartitionSolution) x.copy();
        z.setVariableValue(0, 2);
        z.setVariableValue(1, 1);
        z.setVariableValue(2, 1);
        z.setVariableValue(3, 2);
        z.setVariableValue(4, 1);
        z.setVariableValue(5, 0);
        z.setVariableValue(6, 0);
        z.setVariableValue(7, 0);
        z.setVariableValue(8, 2);
        z.setVariableValue(9, 3);

        List<PartitionSolution> source = new ArrayList<>();
        source.add(x);
        source.add(y);
        HBGFCrossover instance = new HBGFCrossover();
        List<PartitionSolution> expResult = new ArrayList<>();
        expResult.add(z);
        expResult.add(z);
        List<PartitionSolution> result = instance.execute(source);
        assertEquals(expResult, result);
    }

    @Test
    public void testWhenNumberOfGeneratedChildrenEqualsTwo() {
        System.out.println("executeScenario3");
        JMetalRandom.getInstance().setRandomGenerator(new MockRandomNumberGenerator(new double[]{0.0, 2, 4}));
        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test4.toString());
        ClusterProblem problem = new ClusterProblem(false, dataset, new ArrayList<ObjectiveFunction>());
        PartitionSolution x = (PartitionSolution) problem.createSolution();
        PartitionSolution y = (PartitionSolution) problem.createSolution();
        PartitionSolution z1 = (PartitionSolution) x.copy();
        z1.setVariableValue(0, 0);
        z1.setVariableValue(1, 0);
        z1.setVariableValue(2, 0);
        z1.setVariableValue(3, 0);
        z1.setVariableValue(4, 0);
        z1.setVariableValue(5, 1);
        z1.setVariableValue(6, 1);
        z1.setVariableValue(7, 1);
        z1.setVariableValue(8, 1);
        z1.setVariableValue(9, 2);
        PartitionSolution z2 = (PartitionSolution) x.copy();
        z2.setVariableValue(0, 1);
        z2.setVariableValue(1, 0);
        z2.setVariableValue(2, 0);
        z2.setVariableValue(3, 1);
        z2.setVariableValue(4, 0);
        z2.setVariableValue(5, 3);
        z2.setVariableValue(6, 2);
        z2.setVariableValue(7, 2);
        z2.setVariableValue(8, 3);
        z2.setVariableValue(9, 4);

        List<PartitionSolution> source = new ArrayList<>();
        source.add(x);
        source.add(y);
        int numberOfGeneratedChildren = 2;
        HBGFCrossover instance = new HBGFCrossover(numberOfGeneratedChildren);
        List<PartitionSolution> expResult = new ArrayList<>();
        expResult.add(z1);
        expResult.add(z2);
        List<PartitionSolution> result = instance.execute(source);
        assertEquals(expResult, result);
    }

    @Test
    public void testCrossoverProbability() {
        System.out.println("executeScenario3");
        JMetalRandom.getInstance().setRandomGenerator(new MockRandomNumberGenerator(new double[]{0.0}));
        Dataset dataset = DatasetFactory.getInstance().getDataset(DatasetFactory.DATASET.test4.toString());
        ClusterProblem problem = new ClusterProblem(false, dataset, new ArrayList<ObjectiveFunction>());
        PartitionSolution x = (PartitionSolution) problem.createSolution();
        PartitionSolution y = (PartitionSolution) problem.createSolution();
        PartitionSolution z1 = (PartitionSolution) x.copy();
        PartitionSolution z2 = (PartitionSolution) y.copy();
        List<PartitionSolution> source = new ArrayList<>();
        source.add(x);
        source.add(y);
        int numberOfGeneratedChildren = 2;
        double crossoverProbability = 0.0;
        HBGFCrossover instance = new HBGFCrossover(crossoverProbability, numberOfGeneratedChildren);
        List<PartitionSolution> expResult = new ArrayList<>();
        expResult.add(z1);
        expResult.add(z2);
        List<PartitionSolution> result = instance.execute(source);
        assertEquals(expResult, result);
    }
}
