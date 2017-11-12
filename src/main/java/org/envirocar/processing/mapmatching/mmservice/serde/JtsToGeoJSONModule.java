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

    public JtsToGeoJSONModule() {
        addSerializer(new JTSPointSerializer());
        addSerializer(new JTSLineStringSerializer());
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

            // properties
            gen.writeObjectFieldStart(GEOJSON_PROPERTIES);
            gen.writeEndObject();

            gen.writeEndObject();
        }

        @Override
        public Class<Point> handledType() {
            return Point.class;
        }

    }

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

            for (Coordinate point : t.getCoordinates()) {
                gen.writeStartArray();
                gen.writeNumber(point.x);
                gen.writeNumber(point.y);
                gen.writeEndArray();
            }

            gen.writeEndArray();
            gen.writeEndObject();

            // properties
            gen.writeObjectFieldStart(GEOJSON_PROPERTIES);
            gen.writeEndObject();

            gen.writeEndObject();
        }

        @Override
        public Class<LineString> handledType() {
            return LineString.class;
        }

    }
}
