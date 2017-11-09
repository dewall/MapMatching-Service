package org.envirocar.processing.mapmatching.mmservice.graphhopper;

import com.graphhopper.GraphHopper;
import com.graphhopper.matching.MapMatching;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.util.AbstractFlagEncoder;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.weighting.FastestWeighting;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.util.Parameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author dewall
 */
@Configuration
public class GHConfiguration {

    @Bean
    public AbstractFlagEncoder provideFlagEncoder() {
        return new CarFlagEncoder();
    }

    @Bean
    public GraphHopper provideGraphHopper(AbstractFlagEncoder encoder,
            @Value("${graphhopper.osmdata.location}") String osmdata) {
        GraphHopper hopper = new GraphHopperOSM();
        hopper.setDataReaderFile(osmdata);
        hopper.setGraphHopperLocation("./target/mapmatchingtest");
        hopper.setEncodingManager(new EncodingManager(encoder));
        hopper.getCHFactoryDecorator().setEnabled(false);
        hopper.importOrLoad();
        return hopper;
    }

    @Bean
    public MapMatching provideMapMatching(AbstractFlagEncoder encoder, GraphHopper hopper) {
        String algorithm = Parameters.Algorithms.DIJKSTRA_BI;
        Weighting weighting = new FastestWeighting(encoder);
        AlgorithmOptions algoOptions = new AlgorithmOptions(algorithm, weighting);
        MapMatching mapMatching = new MapMatching(hopper, algoOptions);
        return mapMatching;
    }
}
