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
package org.envirocar.processing.mapmatching.mmservice.valhalla;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.envirocar.processing.mapmatching.mmservice.MapMatcherService;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingInput;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 *
 * @author dewall
 */
public class ValhallaMapMatcherService implements MapMatcherService {

    private final ValhallaService service;
    private final GeometryFactory factory;

    @Autowired
    public ValhallaMapMatcherService(ValhallaService service,
            GeometryFactory factory) {
        this.service = service;
        this.factory = factory;
    }

    @Override
    public MapMatchingResult computeMapMatching(MapMatchingInput input) {
        Coordinate[] coordinates = input.getCandidates()
                .stream()
                .map(c -> c.getPoint().getCoordinate())
                .toArray(n -> new Coordinate[n]);
        LineString createLineString = factory.createLineString(coordinates);

        Call<ResponseBody> doMapMatching = service.doMapMatching(createLineString);
        try {
            Response<ResponseBody> execute = doMapMatching.execute();
//            execute.body().
        } catch (IOException ex) {
            Logger.getLogger(ValhallaMapMatcherService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
