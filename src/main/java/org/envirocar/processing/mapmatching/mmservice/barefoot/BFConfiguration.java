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
