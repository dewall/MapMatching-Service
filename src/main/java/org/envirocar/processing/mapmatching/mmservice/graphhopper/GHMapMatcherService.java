package org.envirocar.processing.mapmatching.mmservice.graphhopper;

import com.graphhopper.matching.EdgeMatch;
import com.graphhopper.matching.MapMatching;
import com.graphhopper.matching.MatchResult;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.GPXEntry;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.GHPoint3D;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import java.util.ArrayList;
import java.util.List;
import org.envirocar.processing.mapmatching.mmservice.MapMatcherService;
import org.envirocar.processing.mapmatching.mmservice.MapMatchingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dewall
 */
@Component
public class GHMapMatcherService implements MapMatcherService<GHEnvirocarTrack> {

    private final MapMatching mapMatching;
    private final GeometryFactory geometryFactory;

    @Autowired
    public GHMapMatcherService(MapMatching mapMatching, GeometryFactory geometryFactory) {
        this.mapMatching = mapMatching;
        this.geometryFactory = geometryFactory;
    }

    @Override
    public MapMatchingResult computeMapMatching(GHEnvirocarTrack track) {
        List<GPXEntry> entries = track.getMeasurements();
        MatchResult matchResult = mapMatching.doWork(track.getMeasurements());

        List<EdgeMatch> matches = matchResult.getEdgeMatches();

        List<Coordinate> coordinates = new ArrayList<>();
        for (EdgeMatch match : matches) {
            EdgeIteratorState edgeState = match.getEdgeState();
            PointList pointList = edgeState.fetchWayGeometry(1);

            for (GHPoint3D point : pointList) {
                Coordinate coordinate = new Coordinate(point.lon, point.lat);
                coordinates.add(coordinate);
            }
        }
        LineString lineString = geometryFactory.createLineString(
                coordinates.toArray(new Coordinate[coordinates.size()]));

        return new MapMatchingResult(lineString);
    }
}
