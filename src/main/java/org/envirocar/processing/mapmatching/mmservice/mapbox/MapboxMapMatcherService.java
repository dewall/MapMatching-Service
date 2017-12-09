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
package org.envirocar.processing.mapmatching.mmservice.mapbox;

import com.mapbox.services.api.mapmatching.v5.MapMatchingCriteria;
import com.mapbox.services.api.mapmatching.v5.models.MapMatchingResponse;
import com.mapbox.services.api.rx.mapmatching.v5.MapboxMapMatchingRx;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.PolylineUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import java.util.List;
import org.envirocar.processing.mapmatching.mmservice.MapMatcherService;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingInput;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingResult;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author dewall
 */
public class MapboxMapMatcherService implements MapMatcherService {

    private final String accessToken;

    /**
     * Constructor.
     *
     * @param accessToken
     */
    public MapboxMapMatcherService(@Value("${mapbox.accesstoken}") String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public MapMatchingResult computeMapMatching(MapMatchingInput input) {
        MapMatchingResult result = new MapMatchingResult();

        Position[] positions = input.getCandidates()
                .stream()
                .map(c -> Position.fromLngLat(c.getPoint().getX(), c.getPoint().getY()))
                .toArray((value) -> new Position[value]);

        MapboxMapMatchingRx rxMapMatcher = new MapboxMapMatchingRx.Builder()
                .setAccessToken(accessToken)
                .setCoordinates(positions)
                .setOverview(MapMatchingCriteria.OVERVIEW_FULL)
                .setProfile(MapMatchingCriteria.PROFILE_DRIVING)
                .build();

        rxMapMatcher.getObservable()
                .subscribe((MapMatchingResponse t) -> {
                    List<Position> decode = PolylineUtils.decode(t.getMatchings().get(0).getGeometry(), 6);
                    Coordinate[] coordinates = decode.stream()
                            .map(p -> new Coordinate(p.getLongitude(), p.getLatitude()))
                            .toArray(i -> new Coordinate[i]);
                    LineString lineString = new GeometryFactory().createLineString(coordinates);
                    result.setMatchedLineString(lineString);
                });

        return result;
    }

}
