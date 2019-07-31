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
package br.ufpr.inf.cbio.clusteringcriterias.operator.util;

import java.util.Arrays;

/**
 * A graph represented by compressed sparse row format.
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class GraphCSR {

    private int numberOfVertices; // number of vertices
    private int[] adjacencyIndexes; // indices
    private int[] adacencies; // adjacencies

    public GraphCSR(int nvtxs, int[] xadj, int[] adjncy) {
        this.numberOfVertices = nvtxs;
        this.adjacencyIndexes = xadj;
        this.adacencies = adjncy;
    }

    public int[] getAdjacencyIndexes() {
        return adjacencyIndexes;
    }

    public void setAdjacencyIndexes(int[] adjacencyIndexes) {
        this.adjacencyIndexes = adjacencyIndexes;
    }

    public int[] getAdacencies() {
        return adacencies;
    }

    public void setAdacencies(int[] adacencies) {
        this.adacencies = adacencies;
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public void setNumberOfVertices(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.hashCode() == ((GraphCSR) obj).hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.numberOfVertices;
        hash = 79 * hash + Arrays.hashCode(this.adjacencyIndexes);
        hash = 79 * hash + Arrays.hashCode(this.adacencies);
        return hash;
    }

    /*
    private int numberOfVertices; // number of vertices
    private int[] adjacencyIndexes; // indices
    private int[] adacencies; // adjacencies
     */
    @Override
    public String toString() {
        return "[GraphCSR] numberOfVertices: " + numberOfVertices + ", adjacencyIndexes: " + Arrays.toString(adjacencyIndexes) + ", adacencies: " + Arrays.toString(adacencies) + ".";
    }

}
