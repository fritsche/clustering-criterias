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

import java.util.Comparator;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class DataPointComparator implements Comparator<DataPoint> {

    @Override
    public int compare(DataPoint o1, DataPoint o2) {
        return o1.getId().compareTo(o2.getId());
    }

}
