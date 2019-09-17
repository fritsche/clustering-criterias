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
package br.ufpr.inf.cbio.clusteringcriterias.algorithm;

import br.ufpr.inf.cbio.clusteringcriterias.problem.ClusterProblem;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 * @param <A>
 */
public interface ClusteringAlgorithmConfiguration<A extends Algorithm> {

    public A configure(ClusterProblem problem, CrossoverOperator crossover, MutationOperator mutation, int popSize, SelectionOperator selection, int iterations);

}
