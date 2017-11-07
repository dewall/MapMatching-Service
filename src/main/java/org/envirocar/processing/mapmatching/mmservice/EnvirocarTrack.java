package org.envirocar.processing.mapmatching.mmservice;

import com.graphhopper.util.GPXEntry;
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
public class EnvirocarTrack {

    public static EnvirocarTrack fromString(String input) throws ParseException {
        EnvirocarTrack result = new EnvirocarTrack();

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
                GPXEntry e = new GPXEntry(latitude, longitude, parse.getTime());
                result.entries.add(e);
            } catch (java.text.ParseException ex) {
                Logger.getLogger(EnvirocarTrack.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        return result;
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

    private final List<GPXEntry> entries;

    /**
     * Constructor.
     */
    public EnvirocarTrack() {
        this.entries = new ArrayList<>();
    }

    public List<GPXEntry> getEntries() {
        return entries;
    }

}
