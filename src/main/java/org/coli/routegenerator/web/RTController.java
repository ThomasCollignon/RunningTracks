package org.coli.routegenerator.web;

import lombok.RequiredArgsConstructor;
import org.coli.routegenerator.RTService;
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
public class RTController {

    private final RTService rtService;

    @GetMapping("/")
    public RestModel go(@RequestParam(defaultValue = "10") String distanceKmString) {
        return new RestModel(rtService.getRandomRoute(parseInt(distanceKmString)));
    }

    static class RestModel extends ArrayList<String> {

        public RestModel(Collection<String> c) {
            super(c);
        }
    }

}
