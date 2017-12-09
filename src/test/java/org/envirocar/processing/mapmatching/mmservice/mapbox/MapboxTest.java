package org.envirocar.processing.mapmatching.mmservice.mapbox;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.envirocar.processing.mapmatching.mmservice.MapMatcherService;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingInput;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dewall
 */
public class MapboxTest {

    private static final String TEST_TRACK = "track.geojson";
    private static final String TOKEN = "";

    private MapMatchingInput input;
    private MapMatcherService service;

    @Before
    public void before() throws IOException, ParseException {
        String fileDir = MapboxTest.class.getClassLoader().getResource(TEST_TRACK).getFile();
        String trackString = FileUtils.readFileToString(new File(fileDir));
        input = MapMatchingInput.fromString(trackString);

        this.service = new MapboxMapMatcherService(TOKEN);
    }

    @Test
    public void testMapbox() {
        service.computeMapMatching(input);
    }

}
