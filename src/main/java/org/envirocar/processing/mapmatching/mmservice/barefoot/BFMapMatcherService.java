package org.envirocar.processing.mapmatching.mmservice.barefoot;

import com.bmwcarit.barefoot.matcher.Matcher;
import com.bmwcarit.barefoot.matcher.MatcherCandidate;
import com.bmwcarit.barefoot.matcher.MatcherKState;
import com.esri.core.geometry.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import java.util.ArrayList;
import java.util.List;
import org.envirocar.processing.mapmatching.mmservice.MapMatcherService;
import org.envirocar.processing.mapmatching.mmservice.MapMatchingResult;
import org.springframework.stereotype.Component;

/**
 *
 * @author dewall
 */
@Component
public class BFMapMatcherService implements MapMatcherService<BFEnvirocarTrack> {

    private final GeometryFactory geometryFactory;
    private final Matcher matcher;

    /**
     * Constructor.
     *
     * @param geometryFactory
     */
    public BFMapMatcherService(GeometryFactory geometryFactory, Matcher matcher) {
        this.geometryFactory = geometryFactory;
        this.matcher = matcher;
    }

    @Override
    public MapMatchingResult computeMapMatching(BFEnvirocarTrack track) {
        MapMatchingResult result = new MapMatchingResult();

        MatcherKState state = matcher.mmatch(track.getMeasurements(), 1, 500);
        List<MatcherCandidate> sequence = state.sequence();

        List<Coordinate> coordinates = new ArrayList<>();
        for (MatcherCandidate candidate : state.sequence()) {
            Point geometry = candidate.point().geometry();
            Coordinate coord = new Coordinate();
            coord.x = geometry.getX();
            coord.y = geometry.getY();
            coordinates.add(coord);
        }

        LineString lineString = geometryFactory.createLineString(
                coordinates.toArray(new Coordinate[coordinates.size()]));
        result.setMatchedLineString(lineString);

        return result;
    }

}
