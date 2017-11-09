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
import com.bmwcarit.barefoot.roadmap.Loader;
import com.bmwcarit.barefoot.roadmap.RoadMap;
import com.bmwcarit.barefoot.roadmap.TimePriority;
import com.bmwcarit.barefoot.spatial.Geography;
import com.bmwcarit.barefoot.topology.Dijkstra;
import com.bmwcarit.barefoot.util.SourceException;
import com.bmwcarit.barefoot.util.Tuple;
import java.io.IOException;
import java.util.Map;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author dewall
 */
@Configuration
public class BFConfiguration {

    @Bean
    public PostGISReader providePostGISReader(
            @Value("${barefoot.postgis.host}") String host,
            @Value("${barefoot.postgis.port}") int port,
            @Value("${barefoot.postgis.database}") String database,
            @Value("${barefoot.postgis.table}") String table,
            @Value("${barefoot.postgis.user}") String user,
            @Value("${barefoot.postgis.pass}") String password) throws JSONException, IOException {
        Map<Short, Tuple<Double, Integer>> read = Loader.read("src/main/resources/road-types.json");
        return new PostGISReader(host, port, database, table, user, password, read);
    }

    @Bean
    public RoadMap provideRoadMap(PostGISReader reader) throws SourceException, IOException {
        return RoadMap.Load(reader).construct();
    }

    @Bean
    public Matcher provideMatcher(RoadMap map) {
        return new Matcher(map,
                new Dijkstra<>(),
                new TimePriority(),
                new Geography());
    }
}