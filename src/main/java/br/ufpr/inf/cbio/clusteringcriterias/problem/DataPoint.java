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
package br.ufpr.inf.cbio.clusteringcriterias.problem;

import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.util.point.Point;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class DataPoint {

    private String id;
    private Point point;
    private List<Integer> neighbors;

    DataPoint(String id, Point p) {
        this.id = id;
        this.point = p;
        this.neighbors = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public Point getPoint() {
        return point;
    }

    public void addNeighbor(int i) {
        this.neighbors.add(i);
    }

    public List<Integer> getNeighbors() {
        return neighbors;
    }

}
