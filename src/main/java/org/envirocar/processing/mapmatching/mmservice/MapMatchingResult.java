package org.envirocar.processing.mapmatching.mmservice;

import org.locationtech.jts.geom.LineString;

/**
 *
 * @author dewall
 */
public class MapMatchingResult {
    private LineString matchedString;
    
    public MapMatchingResult(LineString matched){
        this.matchedString = matched;
    }

    public LineString getMatchedString() {
        return matchedString;
    }
    
}
