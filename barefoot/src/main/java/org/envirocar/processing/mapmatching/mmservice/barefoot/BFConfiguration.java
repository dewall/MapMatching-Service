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
package org.envirocar.processing.mapmatching.mmservice.barefoot;

import com.bmwcarit.barefoot.matcher.Matcher;
import com.bmwcarit.barefoot.road.PostGISReader;
import com.bmwcarit.barefoot.roadmap.Distance;
import com.bmwcarit.barefoot.roadmap.Loader;
import com.bmwcarit.barefoot.roadmap.Road;
import com.bmwcarit.barefoot.roadmap.RoadMap;
import com.bmwcarit.barefoot.roadmap.Time;
import com.bmwcarit.barefoot.roadmap.TimePriority;
import com.bmwcarit.barefoot.spatial.Geography;
import com.bmwcarit.barefoot.topology.Cost;
import com.bmwcarit.barefoot.topology.Dijkstra;
import com.bmwcarit.barefoot.util.SourceException;
import com.bmwcarit.barefoot.util.Tuple;
import org.locationtech.jts.geom.GeometryFactory;
import java.io.IOException;
import java.util.Map;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author dewall
 */
@Configuration
@Profile("barefoot")
public class BFConfiguration {

    @Bean
    public PostGISReader providePostGISReader(
            @Value("${barefoot.postgis.host}") String host,
            @Value("${barefoot.postgis.port}") int port,
            @Value("${barefoot.postgis.database}") String database,
            @Value("${barefoot.postgis.table}") String table,
            @Value("${barefoot.postgis.user}") String user,
            @Value("${barefoot.postgis.pass}") String password) throws
            JSONException, IOException {
        Map<Short, Tuple<Double, Integer>> read = Loader.read(
                "etc/map-data/road-types.json");
        return new PostGISReader(host, port, database, table, user, password,
                read);
    }

    @Bean
    public RoadMap provideRoadMap(PostGISReader reader) throws SourceException,
            IOException {
        return RoadMap.Load(reader).construct();
    }

    @Bean
    public Cost<Road> provideCostFunction(
            @Value("${barefoot.matcher.costfunction}") String type) {
        Cost<Road> costfunction = new Distance();
        switch(type){
            case "distance":
                costfunction = new Distance();
                break;
            case "time":
                costfunction = new Time();
                break;
            case "timepriority":
                costfunction = new TimePriority();
                break;  
        }
        return costfunction;
    }

    @Bean
    public Matcher provideMatcher(RoadMap map,
            @Value("${barefoot.matcher.sigma}") double sigma,
            @Value("${barefoot.matcher.lambda}") double lambda,
            @Value("${barefoot.matcher.maxradius}") double maxRadius,
            @Value("${barefoot.matcher.maxdistance}") double maxDistance,
            Cost<Road> costfunction) {
        Matcher matcher = new Matcher(map, new Dijkstra<>(), costfunction, new Geography());
        matcher.setSigma(sigma);
        matcher.setLambda(lambda);
        matcher.setMaxDistance(maxDistance);
        matcher.setMaxRadius(maxRadius);
        return matcher;
    }

    @Bean
    public BFMapMatcherService provideMatcherService(
            @Value("${barefoot.matcher.mindistance}") int minDistance,
            @Value("${barefoot.matcher.mininterval}") int minInterval,
            GeometryFactory geometryFactory,
            Matcher matcher) {
        return new BFMapMatcherService(minDistance, minInterval,
                geometryFactory, matcher);
    }
}
