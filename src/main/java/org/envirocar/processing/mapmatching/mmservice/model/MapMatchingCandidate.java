/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.envirocar.processing.mapmatching.mmservice.model;

import org.locationtech.jts.geom.Point;
import java.util.Date;

/**
 *
 * @author dewall
 */
public class MapMatchingCandidate {

    private String id;
    private Date time;
    private Point point;

    /**
     * Constructor.
     */
    public MapMatchingCandidate() {
    }

    /**
     * Constructor.
     *
     * @param id
     * @param time
     * @param point
     */
    public MapMatchingCandidate(String id, Date time, Point point) {
        this.id = id;
        this.time = time;
        this.point = point;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

}
