package org.coli.routegenerator.restapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coli.routegenerator.service.RouteService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Integer.parseInt;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class RTController {

    private final RouteService routeService;

    @GetMapping("/")
    public RestModel anotherRoute(@RequestParam(defaultValue = "10") String distanceKmString,
                                  @RequestParam(defaultValue = "Chastre") String runZone) {
        log.debug("Controller reached with"
                          + "\n\tdistanceKmString = " + distanceKmString
                          + "\n\tstartingPoint = " + runZone);
        return new RestModel(routeService.getAnotherRouteCoords(parseInt(distanceKmString), runZone));
    }

    static class RestModel extends ArrayList<String> {

        public RestModel(Collection<String> c) {
            super(c);
        }
    }
}
