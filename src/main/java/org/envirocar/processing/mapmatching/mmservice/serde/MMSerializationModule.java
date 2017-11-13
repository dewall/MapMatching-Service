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
package org.envirocar.processing.mapmatching.mmservice.serde;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingResult;
import org.envirocar.processing.mapmatching.mmservice.model.MatchedPoint;
import static org.envirocar.processing.mapmatching.mmservice.serde.GeoJSONConstants.GEOJSON_TYPE;

/**
 *
 * @author dewall
 */
public class MMSerializationModule extends SimpleModule implements
        GeoJSONConstants {

    /**
     * Constructor.
     */
    public MMSerializationModule() {
        addSerializer(new MapMatchingResultSerDe());
    }

    private static final class MapMatchingResultSerDe extends JsonSerializer<MapMatchingResult> {

        @Override
        public void serialize(MapMatchingResult value, JsonGenerator gen,
                SerializerProvider serializers)
                throws IOException, JsonProcessingException {

            gen.writeStartObject();
            gen.writeStringField(GEOJSON_TYPE, "FeatureCollection");
            gen.writeArrayFieldStart(GEOJSON_FEATURES);

            List<MatchedPoint> matchedPoints = value.getMatchedPoints();
            for (MatchedPoint point : matchedPoints) {
                Map<String, Object> userData = new HashMap<>();
                userData.put("osm_street_id", point.getOsmID());

                Point pointOnRoad = point.getPointOnRoad();
                pointOnRoad.setUserData(userData);
                gen.writeObject(pointOnRoad);
            }

            LineString lineString = value.getMatchedLineString();
            gen.writeObject(lineString);

            gen.writeEndArray();
            gen.writeEndObject();
        }

        @Override
        public Class<MapMatchingResult> handledType() {
            return MapMatchingResult.class;
        }

    }
}
