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
package org.envirocar.processing.mapmatching.mmservice.model;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dewall
 */
public class MapMatchingResult {

    private Geometry matchedRoute;
    private List<MatchedPoint> matchedPoints;

    /**
     * Constructor.
     */
    public MapMatchingResult() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param matched
     */
    public MapMatchingResult(LineString matched) {
        this(matched, new ArrayList<>());
    }

    public MapMatchingResult(LineString matchedRoute,
            List<MatchedPoint> matchedPoints) {
        this.matchedRoute = matchedRoute;
        this.matchedPoints = matchedPoints;
    }

    public Geometry getMatchedLineString() {
        return matchedRoute;
    }

    public void setMatchedLineString(LineString matchedString) {
        this.matchedRoute = matchedString;
    }

    public Geometry getMatchedRoute() {
        return matchedRoute;
    }

    public void setMatchedRoute(Geometry matchedRoute) {
        this.matchedRoute = matchedRoute;
    }

    public List<MatchedPoint> getMatchedPoints() {
        return matchedPoints;
    }

    public void setMatchedPoints(List<MatchedPoint> matchedPoints) {
        this.matchedPoints = matchedPoints;
    }

    public void addMatchedPoint(MatchedPoint matchedPoint) {
        this.matchedPoints.add(matchedPoint);
    }

}
