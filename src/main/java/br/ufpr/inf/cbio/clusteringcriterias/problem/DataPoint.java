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

import java.util.Objects;
import org.uma.jmetal.util.point.Point;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class DataPoint {

    private final String id;
    private final Point point;

    DataPoint(String id, Point p) {
        this.id = id;
        this.point = p;
    }

    public String getId() {
        return id;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public String toString() {
        String string = id + "\t[";
        for (int i = 0; i < point.getDimension() - 1; i++) {
            string += point.getValue(i) + ", ";
        }
        string += point.getValue(point.getDimension() - 1) + "]";
        return string;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.hashCode() == ((DataPoint) obj).hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.point);
        return hash;
    }

}
