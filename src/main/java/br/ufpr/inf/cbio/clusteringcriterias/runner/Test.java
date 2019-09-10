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
package br.ufpr.inf.cbio.clusteringcriterias.runner;

import jep.Interpreter;
import jep.JepException;
import jep.MainInterpreter;
import jep.NDArray;
import jep.SharedInterpreter;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class Test {

    public static void main(String[] args) throws JepException {
        MainInterpreter.setJepLibraryPath(Test.class.getClassLoader().getResource("lib/jep.so").getFile());
        try (Interpreter interp = new SharedInterpreter()) {

            interp.exec("from sklearn.metrics import silhouette_score");

            double[] X = new double[]{
                0.0, 0.0,
                0.0, 0.5,
                1.0, 0.0,
                1.0, 0.5,};
            NDArray<double[]> np_X = new NDArray<>(X, 4, 2);
            interp.set("X", np_X);

            int[] labels = new int[]{1, 1, 2, 2};
            NDArray<int[]> np_labels = new NDArray<>(labels, 4);
            interp.set("labels", np_labels);

            interp.exec("silhouette = silhouette_score(X, labels)");
            
            Double silhouette = interp.getValue("silhouette", Double.class);
            System.out.println(silhouette);
        }
    }
}
