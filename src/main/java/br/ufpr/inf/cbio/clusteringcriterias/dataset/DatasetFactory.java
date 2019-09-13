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
        D31,
        ds2c2sc13_V1,
        ds2c2sc13_V2,
        ds2c2sc13_V3,
        ds3c3sc6_V1,
        ds3c3sc6_V2,
        spiralsquare,
        tevc_20_60_1,
        glass,
        iris,
        frogs_V1,
        frogs_V2,
        frogs_V3,
        golub_V1,
        golub_V3,
        leukemia_V1,
        leukemia_V2,
        optdigits,
        libras,
        seeds,
        UKC1,
        test1,
        test2,
        test3,
        test4,
        test5,
        test3D,
    }

    private static DatasetFactory instance = null;

    private DatasetFactory() {
        artificiais = new ArrayList<>(8);
        artificiais.add(DATASET.D31);
        artificiais.add(DATASET.ds2c2sc13_V1);
        artificiais.add(DATASET.ds2c2sc13_V2);
        artificiais.add(DATASET.ds2c2sc13_V3);
        artificiais.add(DATASET.ds3c3sc6_V1);
        artificiais.add(DATASET.ds3c3sc6_V2);
        artificiais.add(DATASET.spiralsquare);
        artificiais.add(DATASET.tevc_20_60_1);

        benchmarks = new ArrayList<>(2);
        benchmarks.add(DATASET.glass);
        benchmarks.add(DATASET.iris);

        reais = new ArrayList<>(11);
        reais.add(DATASET.frogs_V1);
        reais.add(DATASET.frogs_V2);
        reais.add(DATASET.frogs_V3);
        reais.add(DATASET.golub_V1);
        reais.add(DATASET.golub_V3);
        reais.add(DATASET.leukemia_V1);
        reais.add(DATASET.leukemia_V2);
        reais.add(DATASET.libras);
        reais.add(DATASET.optdigits);
        reais.add(DATASET.seeds);
        reais.add(DATASET.UKC1);

        test = new ArrayList<>(5);
        test.add(DATASET.test1);
        test.add(DATASET.test2);
        test.add(DATASET.test3);
        test.add(DATASET.test4);
        test.add(DATASET.test5);
        test.add(DATASET.test3D);
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
        return new Dataset(base + "/dataset/" + dataset + ".txt",
                base + "/partitions/KMWLSNNHDBSCAN/",
                base + "/partitions/TP/" + dataset + "_TP.clu");
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
