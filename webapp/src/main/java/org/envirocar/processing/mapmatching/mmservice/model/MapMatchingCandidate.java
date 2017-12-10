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
    private double accuracy;

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

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

}
