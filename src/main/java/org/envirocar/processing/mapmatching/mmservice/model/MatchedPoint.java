/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.envirocar.processing.mapmatching.mmservice.model;

import org.locationtech.jts.geom.Point;

/**
 *
 * @author dewall
 */
public class MatchedPoint {

    private long osmID;
    private String streetName;

    private Point unmatchedPoint;
    private Point pointOnRoad;

    /**
     * Default Constructor.
     */
    public MatchedPoint() {
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public long getOsmID() {
        return osmID;
    }

    public void setOsmID(long osmID) {
        this.osmID = osmID;
    }

    public Point getUnmatchedPoint() {
        return unmatchedPoint;
    }

    public void setUnmatchedPoint(Point unmatchedPoint) {
        this.unmatchedPoint = unmatchedPoint;
    }

    public Point getPointOnRoad() {
        return pointOnRoad;
    }

    public void setPointOnRoad(Point pointOnRoad) {
        this.pointOnRoad = pointOnRoad;
    }

}
