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
