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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingInput;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingResult;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
@Profile("graphhopper")
public class GHController {

    private final ObjectMapper mapper;
    private final GHMapMatcherService service;

    @Autowired
    public GHController(ObjectMapper mapper, GHMapMatcherService service) {
        this.service = service;
        this.mapper = mapper;
    }

    @RequestMapping(value = "/graphhopper", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity matchEnvirocarTrack(HttpServletRequest request) throws IOException, ParseException {
        String requestString = toInputString(request);
        MapMatchingInput input = MapMatchingInput.fromString(requestString);

        // compute result
        MapMatchingResult result = service.computeMapMatching(input);
        return ResponseEntity.ok(mapper.writeValueAsString(result));
    }

    private String toInputString(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String requestString = IOUtils.toString(inputStream);
        return requestString;
    }

}
