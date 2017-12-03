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
import com.bmwcarit.barefoot.matcher.MatcherSample;
import com.bmwcarit.barefoot.matcher.MatcherTransition;
import com.bmwcarit.barefoot.roadmap.Route;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.envirocar.processing.mapmatching.mmservice.MapMatcherService;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingCandidate;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingInput;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingResult;
import org.envirocar.processing.mapmatching.mmservice.model.MatchedPoint;
import org.json.JSONException;

/**
 *
 * @author dewall
 */
public class BFMapMatcherService implements MapMatcherService {

    private final GeometryFactory geometryFactory;
    private final Matcher matcher;

    private final int minDistance;
    private final int minInterval;

    /**
     * Constructor.
     *
     * @param minDistance
     * @param geometryFactory
     * @param minInterval
     * @param matcher
     */
    public BFMapMatcherService(int minDistance, int minInterval,
            GeometryFactory geometryFactory, Matcher matcher) {
        this.geometryFactory = geometryFactory;
        this.matcher = matcher;

        this.minDistance = minDistance;
        this.minInterval = minInterval;
    }

    @Override
    public MapMatchingResult computeMapMatching(MapMatchingInput input) {
        MapMatchingResult result = new MapMatchingResult();

        MatcherKState state = this.matcher.mmatch(
                getAsMatcherSamples(input.getCandidates()), this.minDistance,
                this.minInterval);

        List<Coordinate> coordinates = new ArrayList<>();
        for (MatcherCandidate candidate : state.sequence()) {
            // matching point
            long osmid = candidate.point().edge().base().refid();
            Point pointOnRoad = candidate.point().geometry();

            MatchedPoint matchedPoint = new MatchedPoint();
            matchedPoint.setOsmID(osmid);
            matchedPoint.setPointOnRoad(geometryFactory.createPoint(
                    new Coordinate(pointOnRoad.getX(), pointOnRoad.getY())));
            result.addMatchedPoint(matchedPoint);

            // Include the transition between two successive points.
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

    private void printResults(MatcherKState state) {
        try {
            PrintWriter out = new PrintWriter("kstate-geojson.json");
            out.println(state.toGeoJSON());

            PrintWriter json = new PrintWriter("kstate-json.json");
            json.println(state.toJSON());

            out.close();
            json.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BFMapMatcherService.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(BFMapMatcherService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<MatcherSample> getAsMatcherSamples(
            List<MapMatchingCandidate> candidates) {
        return candidates.stream()
                .map((t) -> {
                    return new MatcherSample(
                            t.getId(),
                            t.getTime().getTime(),
                            new Point(
                                    t.getPoint().getX(),
                                    t.getPoint().getY()));
                }).collect(Collectors.toList());
    }
}
