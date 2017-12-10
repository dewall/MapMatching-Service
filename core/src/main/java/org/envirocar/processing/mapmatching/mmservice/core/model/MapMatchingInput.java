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
package org.envirocar.processing.mapmatching.mmservice.core.model;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author dewall
 */
public class MapMatchingInput {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

    public static MapMatchingInput fromString(String input) throws
            ParseException {
        GeometryFactory factory = new GeometryFactory(); // todo
        MapMatchingInput result = new MapMatchingInput();

        JSONParser jsonParser = new JSONParser();

        JSONObject track = (JSONObject) jsonParser.parse(input);
        JSONArray features = (JSONArray) track.get("features");

        features.forEach((f) -> {
            JSONObject feature = (JSONObject) f;
            JSONObject geometry = (JSONObject) feature.get("geometry");

            // Get the geometry
            JSONArray coordinates = (JSONArray) geometry.get("coordinates");
            Double latitude = (Double) coordinates.get(1);
            Double longitude = (Double) coordinates.get(0);

            // Get the properties
            JSONObject properties = (JSONObject) feature.get("properties");
            String id = (String) properties.get("id");
            String timeString = (String) properties.get("time");

            // Get the GPS Accuracy
            JSONObject phenomenons = (JSONObject) properties.get("phenomenons");
            JSONObject gpsAccuracy = (JSONObject) phenomenons.get("GPS Accuracy");
            double accuracy = (Double) gpsAccuracy.get("value");

            Date time = null;
            try {
                time = DATE_FORMAT.parse(timeString);
                Point point = factory.createPoint(
                        new Coordinate(longitude, latitude));
                MapMatchingCandidate e = new MapMatchingCandidate(
                        id, time, point);
                e.setAccuracy(accuracy);
                result.addCandidate(e);
            } catch (java.text.ParseException ex) {
                Logger.getLogger(MapMatchingInput.class.getName()).log(
                        Level.SEVERE, null, ex);
            }

        });

        return result;
    }

    private List<MapMatchingCandidate> candidates;

    /**
     * Constructor.
     */
    public MapMatchingInput() {
        this.candidates = new ArrayList<>();
    }

    public List<MapMatchingCandidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<MapMatchingCandidate> candidates) {
        this.candidates = candidates;
    }

    public void addCandidate(MapMatchingCandidate candidate) {
        this.candidates.add(candidate);
    }

}
