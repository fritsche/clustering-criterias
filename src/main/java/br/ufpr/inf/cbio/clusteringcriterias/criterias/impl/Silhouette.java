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

import br.ufpr.inf.cbio.clusteringcriterias.criterias.JepUtils;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.ObjectiveFunction;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.DataPoint;
import br.ufpr.inf.cbio.clusteringcriterias.dataset.Dataset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jep.Interpreter;
import jep.JepException;
import jep.MainInterpreter;
import jep.NDArray;
import jep.SharedInterpreter;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.point.Point;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class Silhouette implements ObjectiveFunction<IntegerSolution> {

    private final NDArray<double[]> featureArray;

    public Silhouette(Dataset dataset) throws JepException {
        JepUtils.initializePythonInterpreter();
        this.featureArray = datsetToFeatureArray(dataset);
    }

    @Override
    public double evaluate(IntegerSolution s) {
        int[] array = new int[s.getNumberOfVariables() - 1];
        for (int i = 0; i < s.getNumberOfVariables() - 1; i++) {
            array[i] = s.getVariableValue(i);
        }
        NDArray<int[]> labels = new NDArray<>(array, array.length);

        try (Interpreter interp = new SharedInterpreter()) {
            interp.exec("from sklearn.metrics import silhouette_score");
            interp.set("X", featureArray);
            interp.set("labels", labels);
            return (double) interp.getValue("silhouette_score(X, labels)");
        } catch (JepException ex) {
            Logger.getLogger(Silhouette.class.getName()).log(Level.SEVERE, "Error to execute python script!", ex);
        }
        return Double.NaN;
    }

    private NDArray<double[]> datsetToFeatureArray(Dataset dataset) {
        List<DataPoint> dataPoints = dataset.getDataPoints();
        double[] array = new double[dataPoints.size() * dataPoints.get(0).getPoint().getDimension()];
        int count = 0;
        for (DataPoint dp : dataPoints) {
            Point p = dp.getPoint();
            for (int i = 0; i < p.getDimension(); i++) {
                array[count++] = p.getValue(i);
            }
        }
        return new NDArray<>(array, dataPoints.size(), dataPoints.get(0).getPoint().getDimension());
    }

}
