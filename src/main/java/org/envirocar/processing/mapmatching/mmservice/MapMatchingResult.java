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
package org.envirocar.processing.mapmatching.mmservice;

import org.locationtech.jts.geom.LineString;

/**
 *
 * @author dewall
 */
public class MapMatchingResult {
    
    // matchedPoints
    // private List<MatchedPoint> matchedPoints;
    private LineString matchedString;

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
        this.matchedString = matched;
    }

    public LineString getMatchedLineString() {
        return matchedString;
    }

    public void setMatchedLineString(LineString matchedString) {
        this.matchedString = matchedString;
    }
    
}
