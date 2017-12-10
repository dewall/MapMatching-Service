/*
 * Copyright (C) 2017 the enviroCar community
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.envirocar.processing.mapmatching.mmservice.core.model;

import org.locationtech.jts.geom.Point;

/**
 *
 * @author dewall
 */
public class MatchedPoint {

    private long osmID;
    private String streetName;

    private Point unmatchedPoint;
    private Point pointOnRoad;

    /**
     * Default Constructor.
     */
    public MatchedPoint() {
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public long getOsmID() {
        return osmID;
    }

    public void setOsmID(long osmID) {
        this.osmID = osmID;
    }

    public Point getUnmatchedPoint() {
        return unmatchedPoint;
    }

    public void setUnmatchedPoint(Point unmatchedPoint) {
        this.unmatchedPoint = unmatchedPoint;
    }

    public Point getPointOnRoad() {
        return pointOnRoad;
    }

    public void setPointOnRoad(Point pointOnRoad) {
        this.pointOnRoad = pointOnRoad;
    }

}
