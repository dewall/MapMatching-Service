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
package org.envirocar.processing.mapmatching.mmservice.core.utils;

import java.io.IOException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author dewall
 */
public class RequestUtils {

    private static final String UTF_CHARSET = "UTF-8";

    /**
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static final String toInputString(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String requestString = IOUtils.toString(inputStream, UTF_CHARSET);
        return requestString;
    }
}
