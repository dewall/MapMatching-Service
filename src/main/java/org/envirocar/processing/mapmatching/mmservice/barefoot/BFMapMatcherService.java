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
package org.envirocar.processing.mapmatching.mmservice.barefoot;

import com.bmwcarit.barefoot.matcher.Matcher;
import com.bmwcarit.barefoot.matcher.MatcherCandidate;
import com.bmwcarit.barefoot.matcher.MatcherKState;
import com.bmwcarit.barefoot.matcher.MatcherTransition;
import com.bmwcarit.barefoot.road.BaseRoad;
import com.bmwcarit.barefoot.roadmap.Road;
import com.bmwcarit.barefoot.roadmap.Route;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import java.util.ArrayList;
import java.util.List;
import org.envirocar.processing.mapmatching.mmservice.MapMatcherService;
import org.envirocar.processing.mapmatching.mmservice.MapMatchingResult;
import org.springframework.stereotype.Component;

/**
 *
 * @author dewall
 */
@Component
public class BFMapMatcherService implements MapMatcherService<BFEnvirocarTrack> {

    private final GeometryFactory geometryFactory;
    private final Matcher matcher;

    /**
     * Constructor.
     *
     * @param geometryFactory
     * @param matcher
     */
    public BFMapMatcherService(GeometryFactory geometryFactory, Matcher matcher) {
        this.geometryFactory = geometryFactory;
        this.matcher = matcher;
    }

    @Override
    public MapMatchingResult computeMapMatching(BFEnvirocarTrack track) {
        MapMatchingResult result = new MapMatchingResult();
        
        MatcherKState state = this.matcher.mmatch(track.getMeasurements(), 1, 500);
        List<MatcherCandidate> sequence = state.sequence();

        List<Coordinate> coordinates = new ArrayList<>();
        for (MatcherCandidate candidate : state.sequence()) {
            // matching point
            // road
            MatcherTransition transition = candidate.transition();
            if (transition != null) {
                Route route = transition.route();
                Polyline polyline = route.geometry();

                for (int i = 0; i < polyline.getPointCount(); i++) {
                    Point point = polyline.getPoint(i);
                    Coordinate coord = new Coordinate();
                    coord.x = point.getX();
                    coord.y = point.getY();
                    coordinates.add(coord);
                }
            }
        }

        LineString lineString = geometryFactory.createLineString(
                coordinates.toArray(new Coordinate[coordinates.size()]));
        result.setMatchedLineString(lineString);

        return result;
    }

}
