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

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.envirocar.processing.mapmatching.mmservice.core.MapMatcherService;
import org.envirocar.processing.mapmatching.mmservice.core.model.MapMatchingInput;
import org.envirocar.processing.mapmatching.mmservice.mapbox.MapboxMapMatcherService;
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

        this.service = new MapboxMapMatcherService(TOKEN, 100, 5);
    }

//    @Test
    public void testMapbox() {
        service.computeMapMatching(input);
    }

}
