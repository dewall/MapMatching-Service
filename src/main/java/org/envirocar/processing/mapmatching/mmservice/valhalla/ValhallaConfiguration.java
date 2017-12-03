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
package org.envirocar.processing.mapmatching.mmservice.valhalla;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 *
 * @author dewall
 */
@Configuration
public class ValhallaConfiguration {

    @Bean
    public Retrofit provideRetrofit(@Value("${valhalla.service.url}") String baseUrl, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
    }

    @Bean
    public ValhallaService provideValhallaService(Retrofit retrofit) {
        return retrofit.create(ValhallaService.class);
    }

    @Bean
    public ValhallaMapMatcherService provideValhallaMapMatcher(ValhallaService service, GeometryFactory factory) {
        return new ValhallaMapMatcherService(service, factory);
    }
}
