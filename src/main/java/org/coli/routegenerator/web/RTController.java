package org.coli.routegenerator.web;

import org.coli.routegenerator.RTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.Integer.valueOf;

@RestController
@CrossOrigin
public class RTController {

    @Autowired
    private RTService rtService;

    @GetMapping("/")
    public RestModel go(@RequestParam(defaultValue = "10") String distanceKmString) {
        return new RestModel(rtService.getRandomRoute(valueOf(distanceKmString)));
    }
}
