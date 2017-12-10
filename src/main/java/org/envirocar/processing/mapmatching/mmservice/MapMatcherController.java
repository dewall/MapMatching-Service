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

import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.envirocar.processing.mapmatching.mmservice.barefoot.BFMapMatcherService;
import org.envirocar.processing.mapmatching.mmservice.graphhopper.GHMapMatcherService;
import org.envirocar.processing.mapmatching.mmservice.mapbox.MapboxMapMatcherService;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingInput;
import org.envirocar.processing.mapmatching.mmservice.valhalla.ValhallaMapMatcherService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dewall
 */
@RestController
@RequestMapping("/mapmatching")
public class MapMatcherController {

    private final MapMatcherService mapMatcherGH;
    private final BFMapMatcherService mapMatcherBF;
    private final ValhallaMapMatcherService mapMatcherVH;
    private final ObjectMapper objectMapper;
    private final MapboxMapMatcherService mapMatcherMB;

    /**
     *
     * @param mapMatcherGH
     * @param mapMatcherVH
     * @param mapMatcherBF
     * @param mapMatcherMB
     * @param objectMapper
     */
    @Autowired
    public MapMatcherController(
//            GHMapMatcherService mapMatcherGH,
//            BFMapMatcherService mapMatcherBF,
//            ValhallaMapMatcherService mapMatcherVH,
            MapboxMapMatcherService mapMatcherMB,
            ObjectMapper objectMapper) {
        this.mapMatcherGH = null;
        this.mapMatcherBF = null;
        this.mapMatcherVH = null;
        this.mapMatcherMB = mapMatcherMB;
        this.objectMapper = objectMapper;
    }



    @RequestMapping(value = "/barefoot", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity matchWithBarefoot(HttpServletRequest request) throws IOException, ParseException {
        String requestString = toInputString(request);
        MapMatchingInput input = MapMatchingInput.fromString(requestString);

        // compute result
        MapMatchingResult result = mapMatcherBF.computeMapMatching(input);
        return ResponseEntity.ok(objectMapper.writeValueAsString(result));
    }

    @RequestMapping(value = "/valhalla", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity matchWithValhalla(HttpServletRequest request) throws IOException, ParseException {
        String requestString = toInputString(request);
        MapMatchingInput input = MapMatchingInput.fromString(requestString);

        // compute result
        MapMatchingResult result = mapMatcherVH.computeMapMatching(input);
        return ResponseEntity.ok(objectMapper.writeValueAsString(result));
    }

    @RequestMapping(value = "/mapbox", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity matchWithMapBox(HttpServletRequest request) throws IOException, ParseException {
        String requestString = toInputString(request);
        MapMatchingInput input = MapMatchingInput.fromString(requestString);

        // compute result
        MapMatchingResult result = mapMatcherMB.computeMapMatching(input);
        return ResponseEntity.ok(objectMapper.writeValueAsString(result));
    }

    private String toInputString(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String requestString = IOUtils.toString(inputStream);
        return requestString;
    }
}
