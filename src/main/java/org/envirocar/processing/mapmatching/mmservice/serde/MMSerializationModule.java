package org.envirocar.processing.mapmatching.mmservice.serde;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import java.io.IOException;
import org.envirocar.processing.mapmatching.mmservice.MapMatchingResult;

/**
 *
 * @author hafenkran
 */
public class MMSerializationModule extends SimpleModule implements GeoJSONConstants {

    public MMSerializationModule() {
        addSerializer(new MapMatchingResultSerDe());
    }

    private static final class MapMatchingResultSerDe extends JsonSerializer<MapMatchingResult> {

        @Override
        public void serialize(MapMatchingResult value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            LineString lineString = value.getMatchedString();

            gen.writeStartObject();
            gen.writeStringField(GEOJSON_TYPE, lineString.getGeometryType());
            gen.writeArrayFieldStart(GEOJSON_COORDINATES);

            for (Coordinate point : lineString.getCoordinates()) {
                gen.writeStartArray();
                gen.writeNumber(point.x);
                gen.writeNumber(point.y);
                gen.writeEndArray();
            }

            gen.writeEndArray();
            gen.writeEndObject();
        }

        @Override
        public Class<MapMatchingResult> handledType() {
            return MapMatchingResult.class;
        }

    }
}
