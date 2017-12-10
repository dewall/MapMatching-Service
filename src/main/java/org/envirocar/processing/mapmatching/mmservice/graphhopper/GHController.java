package org.envirocar.processing.mapmatching.mmservice.graphhopper;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingInput;
import org.envirocar.processing.mapmatching.mmservice.model.MapMatchingResult;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dewall
 */
@RestController
@RequestMapping("/mapmatching")
@Profile("graphhopper")
public class GHController {

    private final ObjectMapper mapper;
    private final GHMapMatcherService service;

    @Autowired
    public GHController(ObjectMapper mapper, GHMapMatcherService service) {
        this.service = service;
        this.mapper = mapper;
    }

    @RequestMapping(value = "/graphhopper", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity matchEnvirocarTrack(HttpServletRequest request) throws IOException, ParseException {
        String requestString = toInputString(request);
        MapMatchingInput input = MapMatchingInput.fromString(requestString);

        // compute result
        MapMatchingResult result = service.computeMapMatching(input);
        return ResponseEntity.ok(mapper.writeValueAsString(result));
    }

    private String toInputString(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String requestString = IOUtils.toString(inputStream);
        return requestString;
    }

}
