package org.coli.routegenerator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class RTApplication {

    public static void main(String[] args) {
        SpringApplication.run(RTApplication.class, args);
        log.debug("""
                                    
                                    
                  Running Tracks Generator app started with DEBUG logging level.
                  Don't pay attention to the Tomcat error logs above, it's just noise.
                  """);
    }
}
