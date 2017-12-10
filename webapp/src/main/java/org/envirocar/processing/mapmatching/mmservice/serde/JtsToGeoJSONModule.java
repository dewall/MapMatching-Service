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
 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.envirocar.processing.mapmatching.mmservice.serde;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Point;
import java.io.IOException;
import static org.envirocar.processing.mapmatching.mmservice.serde.GeoJSONConstants.GEOJSON_COORDINATES;
import static org.envirocar.processing.mapmatching.mmservice.serde.GeoJSONConstants.GEOJSON_FEATURE;
import static org.envirocar.processing.mapmatching.mmservice.serde.GeoJSONConstants.GEOJSON_GEOMETRY;
import static org.envirocar.processing.mapmatching.mmservice.serde.GeoJSONConstants.GEOJSON_PROPERTIES;
import static org.envirocar.processing.mapmatching.mmservice.serde.GeoJSONConstants.GEOJSON_TYPE;

/**
 *
 * @author dewall
 */
public class JtsToGeoJSONModule extends SimpleModule implements GeoJSONConstants {

    /**
     * Constructor.
     */
    public JtsToGeoJSONModule() {
        addSerializer(new JTSPointSerializer());
        addSerializer(new JTSLineStringSerializer());
        addSerializer(new JTSMultiLineStringSerializer());
    }

    public static final class JTSPointSerializer
            extends JsonSerializer<Point> {

        @Override
        public void serialize(Point t, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            gen.writeStartObject();

            gen.writeStringField(GEOJSON_TYPE, GEOJSON_FEATURE);
            gen.writeObjectFieldStart(GEOJSON_GEOMETRY);
            gen.writeStringField(GEOJSON_TYPE, t.getGeometryType());

            // Coordinates
            gen.writeArrayFieldStart(GEOJSON_COORDINATES);
            gen.writeNumber(t.getX());
            gen.writeNumber(t.getY());
            gen.writeEndArray();
            gen.writeEndObject();

            // Properties
            Object userData = t.getUserData();
            if (userData == null) {
                gen.writeObjectFieldStart(GEOJSON_PROPERTIES);
                gen.writeEndObject();
            } else {
                gen.writeObjectField(GEOJSON_PROPERTIES, t.getUserData());
            }

            gen.writeEndObject();
        }

        @Override
        public Class<Point> handledType() {
            return Point.class;
        }

    }

    /**
     * 
     */
    public static final class JTSLineStringSerializer
            extends JsonSerializer<LineString> {

        @Override
        public void serialize(LineString t, JsonGenerator gen,
                SerializerProvider sp) throws IOException,
                JsonProcessingException {

            gen.writeStartObject();
            gen.writeStringField(GEOJSON_TYPE, GEOJSON_FEATURE);
            gen.writeObjectFieldStart(GEOJSON_GEOMETRY);
            gen.writeStringField(GEOJSON_TYPE, t.getGeometryType());
            gen.writeArrayFieldStart(GEOJSON_COORDINATES);

            // Coordinates
            for (Coordinate point : t.getCoordinates()) {
                gen.writeStartArray();
                gen.writeNumber(point.x);
                gen.writeNumber(point.y);
                gen.writeEndArray();
            }

            gen.writeEndArray();
            gen.writeEndObject();

            // Properties
            Object userData = t.getUserData();
            if (userData == null) {
                gen.writeObjectFieldStart(GEOJSON_PROPERTIES);
                gen.writeEndObject();
            } else {
                gen.writeObjectField(GEOJSON_PROPERTIES, t.getUserData());
            }

            gen.writeEndObject();
        }

        @Override
        public Class<LineString> handledType() {
            return LineString.class;
        }

    }

    public static final class JTSMultiLineStringSerializer extends JsonSerializer<MultiLineString> {

        @Override
        public void serialize(MultiLineString t, JsonGenerator gen, SerializerProvider serializers) throws IOException,
                JsonProcessingException {

            gen.writeStartObject();
            gen.writeStringField(GEOJSON_TYPE, GEOJSON_FEATURE);
            gen.writeObjectFieldStart(GEOJSON_GEOMETRY);
            gen.writeStringField(GEOJSON_TYPE, t.getGeometryType());
            gen.writeArrayFieldStart(GEOJSON_COORDINATES);

            for (int i = 0, size = t.getNumGeometries(); i < size; i++) {
                LineString lineString = (LineString) t.getGeometryN(i);
                gen.writeStartArray();
                // Coordinates
                for (Coordinate point : lineString.getCoordinates()) {
                    gen.writeStartArray();
                    gen.writeNumber(point.x);
                    gen.writeNumber(point.y);
                    gen.writeEndArray();
                }
                gen.writeEndArray();
            }

            gen.writeEndArray();
            gen.writeEndObject();

            // Properties
            Object userData = t.getUserData();
            if (userData == null) {
                gen.writeObjectFieldStart(GEOJSON_PROPERTIES);
                gen.writeEndObject();
            } else {
                gen.writeObjectField(GEOJSON_PROPERTIES, t.getUserData());
            }

            gen.writeEndObject();
        }

        @Override
        public Class<MultiLineString> handledType() {
            return MultiLineString.class;
        }
    }
}
