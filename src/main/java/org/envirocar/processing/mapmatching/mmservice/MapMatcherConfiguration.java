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
package org.envirocar.processing.mapmatching.mmservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.locationtech.jts.geom.GeometryFactory;
import java.util.List;
import org.envirocar.processing.mapmatching.mmservice.serde.MMSerializationModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.CompositeUriComponentsContributor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 *
 * @author dewall
 */
@Configuration
public class MapMatcherConfiguration extends WebMvcConfigurationSupport {

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

    @Bean
    public ObjectMapper provideObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new MMSerializationModule());
    }

    @Bean
    public GeometryFactory provideGeometryFactory() {
        return new GeometryFactory();
    }

}
