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
package br.ufpr.inf.cbio.clusteringcriterias.dataset;

import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.util.JMetalException;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class DatasetFactory {

    private final List<DATASET> artificiais;
    private final List<DATASET> benchmarks;
    private final List<DATASET> reais;
    private final List<DATASET> test;

    public enum DATASET {
        ds2c2sc13,
        ds3c3sc6,
        ds4c2sc8,
        spiralsquare,
        glass,
        iris,
        golub,
        lung,
        test1,
        test2,
        test3,
        test4,
        test5
    }

    private static DatasetFactory instance = null;

    private DatasetFactory() {
        artificiais = new ArrayList<>(3);
        artificiais.add(DATASET.ds2c2sc13);
        artificiais.add(DATASET.ds3c3sc6);
        artificiais.add(DATASET.ds4c2sc8);

        benchmarks = new ArrayList<>(2);
        benchmarks.add(DATASET.glass);
        benchmarks.add(DATASET.iris);

        reais = new ArrayList<>(2);
        reais.add(DATASET.golub);
        reais.add(DATASET.lung);

        test = new ArrayList<>(5);
        test.add(DATASET.test1);
        test.add(DATASET.test2);
        test.add(DATASET.test3);
        test.add(DATASET.test4);
        test.add(DATASET.test5);
    }

    public static DatasetFactory getInstance() {
        if (instance == null) {
            instance = new DatasetFactory();
        }
        return instance;
    }

    public Dataset getDataset(String dataset) {
        String base = null;

        if (datasetEquals(dataset, artificiais)) {
            base = "datasets/artificiais/" + dataset;
        } else if (datasetEquals(dataset, benchmarks)) {
            base = "datasets/benchmarks/" + dataset;
        } else if (datasetEquals(dataset, reais)) {
            base = "datasets/reais/" + dataset;
        } else {
            if (datasetEquals(dataset, test)) {
                base = "datasets/test/" + dataset;
                return new Dataset(base + "/dataset.txt", base + "/initialPartitions/");
            } else {
                throw new JMetalException("There is no configuration for '" + dataset + "' dataset.");
            }
        }
        return new Dataset(base + "/dataset/" + dataset + ".txt", base + "/partitions/ALKMSLSNN/");
    }

    private boolean datasetEquals(String dataset, List<DATASET> values) {
        for (DATASET v : values) {
            if (v.toString().equals(dataset)) {
                return true;
            }
        }
        return false;
    }

}
