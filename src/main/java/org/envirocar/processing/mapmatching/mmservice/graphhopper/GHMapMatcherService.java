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
package org.envirocar.processing.mapmatching.mmservice.graphhopper;

import com.graphhopper.matching.EdgeMatch;
import com.graphhopper.matching.MapMatching;
import com.graphhopper.matching.MatchResult;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.GPXEntry;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.GHPoint3D;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.envirocar.processing.mapmatching.mmservice.MapMatcherService;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingCandidate;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingInput;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dewall
 */
public class GHMapMatcherService implements MapMatcherService {

    private final MapMatching mapMatching;
    private final GeometryFactory geometryFactory;

    /**
     * Constructor.
     *
     * @param mapMatching
     * @param geometryFactory
     */
    public GHMapMatcherService(MapMatching mapMatching,
            GeometryFactory geometryFactory) {
        this.mapMatching = mapMatching;
        this.geometryFactory = geometryFactory;
    }

    @Override
    public MapMatchingResult computeMapMatching(MapMatchingInput input) {
        List<GPXEntry> entries = getAsGPXEntry(input.getCandidates());
        MatchResult matchResult = mapMatching.doWork(entries);

        List<EdgeMatch> matches = matchResult.getEdgeMatches();

        List<Coordinate> coordinates = new ArrayList<>();
        for (EdgeMatch match : matches) {
            EdgeIteratorState edgeState = match.getEdgeState();
            PointList pointList = edgeState.fetchWayGeometry(1);

            for (GHPoint3D point : pointList) {
                Coordinate coordinate = new Coordinate(point.lon, point.lat);
                coordinates.add(coordinate);
            }
        }
        LineString lineString = geometryFactory.createLineString(
                coordinates.toArray(new Coordinate[coordinates.size()]));

        return new MapMatchingResult(lineString);
    }

    private List<GPXEntry> getAsGPXEntry(List<MapMatchingCandidate> candidates) {
        return candidates.stream()
                .map((t) -> new GPXEntry(
                t.getPoint().getY(),
                t.getPoint().getX(),
                t.getTime().getTime()))
                .collect(Collectors.toList());
    }
}
