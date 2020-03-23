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

import br.ufpr.inf.cbio.clusteringcriterias.criterias.JepUtils;
import br.ufpr.inf.cbio.clusteringcriterias.criterias.impl.DaviesBouldin;
import br.ufpr.inf.cbio.clusteringcriterias.solution.PartitionSolution;
import jep.Interpreter;
import jep.JepException;
import jep.NDArray;
import jep.SharedInterpreter;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a CSPA cluster ensemble for crossover using
 *
 * @author Adriano F. Kultzak <adriano_fk@hotmail.com>
 */
public class MCLACrossoverII implements CrossoverOperator<PartitionSolution> {

    private final int numberOfRequiredParents;
    private final NDArray<double[]> featureArray;
//    private final Dataset dataset;

    public MCLACrossoverII() throws JepException {
        JepUtils.initializePython3Interpreter();
        this.featureArray = null;
        this.numberOfRequiredParents = 2;
//        this.dataset = dataset;
    }


    public PartitionSolution mcla(PartitionSolution a, PartitionSolution b) {



        return a;
    }


    @Override
    public int getNumberOfRequiredParents() {
        return numberOfRequiredParents;
    }

    @Override
    public int getNumberOfGeneratedChildren() {
        return 1;
    }

    @Override
    public List<PartitionSolution> execute(List<PartitionSolution> source) {
        if (null == source) {
            throw new JMetalException("Null parameter");
        } else if (source.size() != numberOfRequiredParents) {
            throw new JMetalException("There must be " + numberOfRequiredParents + " parents instead of " + source.size());
        }
        return doCrossover(source.get(0), source.get(1));
    }

    public List<PartitionSolution> doCrossover(PartitionSolution parent1, PartitionSolution parent2) {
        List<PartitionSolution> offspring = new ArrayList<>();
        offspring.add(mcla(parent1, parent2));
        return offspring;
    }

}
