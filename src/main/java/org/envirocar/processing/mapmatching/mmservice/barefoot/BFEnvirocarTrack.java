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

import com.bmwcarit.barefoot.matcher.MatcherSample;
import com.esri.core.geometry.Point;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.envirocar.processing.mapmatching.mmservice.graphhopper.GHEnvirocarTrack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author dewall
 */
public class BFEnvirocarTrack {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

    public static BFEnvirocarTrack fromString(String input) throws ParseException {
        BFEnvirocarTrack result = new BFEnvirocarTrack();

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

            Date parse = null;
            try {
                parse = DATE_FORMAT.parse(timeString);
                Point point = new Point(longitude, latitude);
                MatcherSample e = new MatcherSample(id, parse.getTime(), point);
                result.measurements.add(e);
            } catch (java.text.ParseException ex) {
                Logger.getLogger(GHEnvirocarTrack.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        return result;

    }

    private final List<MatcherSample> measurements;

    /**
     * Constructor.
     */
    public BFEnvirocarTrack() {
        this.measurements = new ArrayList<>();
    }

    public List<MatcherSample> getMeasurements() {
        return measurements;
    }
}
