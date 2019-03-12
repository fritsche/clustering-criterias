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

import br.ufpr.inf.cbio.clusteringcriterias.problem.DataPoint;
import java.util.Arrays;

/**
 * A graph represented by compressed sparse row format.
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class GraphCSR {

    private int nvtxs; // number of vertices
    private int[] xadj; // indices
    private int[] adjncy; // adjacencies

    public GraphCSR(int nvtxs, int[] xadj, int[] adjncy) {
        this.nvtxs = nvtxs;
        this.xadj = xadj;
        this.adjncy = adjncy;
    }

    public int[] getXadj() {
        return xadj;
    }

    public void setXadj(int[] xadj) {
        this.xadj = xadj;
    }

    public int[] getAdjncy() {
        return adjncy;
    }

    public void setAdjncy(int[] adjncy) {
        this.adjncy = adjncy;
    }

    public int getNvtxs() {
        return nvtxs;
    }

    public void setNvtxs(int nvtxs) {
        this.nvtxs = nvtxs;
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
        hash = 79 * hash + this.nvtxs;
        hash = 79 * hash + Arrays.hashCode(this.xadj);
        hash = 79 * hash + Arrays.hashCode(this.adjncy);
        return hash;
    }

}
