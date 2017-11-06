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

import javax.servlet.http.HttpServletRequest;
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

    private final MapMatcherService mapMatcher;

    /**
     *
     * @param mapMatcher
     */
    @Autowired
    public MapMatcherController(MapMatcherService mapMatcher) {
        this.mapMatcher = mapMatcher;
    }

    @RequestMapping(value = "/ec-matcher", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity matchEnvirocarTrack(HttpServletRequest request) {
        return null;
    }

}
