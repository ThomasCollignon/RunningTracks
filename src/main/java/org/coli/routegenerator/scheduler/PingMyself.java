package org.coli.routegenerator.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * Every 30min (sometimes less, like 20min.) Heroku stops the app, it restarts at next query. Issue is that it takes
 * 30min to reload the cache. Preventing the app from going down allows the cached data to always be directly
 * available.
 */
@Component
@Slf4j
public class PingMyself {

    private static final int SELF_PING_DELAY_MINUTES = 15;

    @Scheduled(fixedDelay = SELF_PING_DELAY_MINUTES, initialDelay = SELF_PING_DELAY_MINUTES, timeUnit = MINUTES)
    private void pingMyself() {
        final String URL = "https://main--nimble-choux-2eecbe.netlify.app/";
        RestTemplate restTemplate = new RestTemplate();
        log.info("Self ping to stay alive");
        ResponseEntity<String> response = restTemplate.getForEntity(URL, String.class);
        log.info("Self ping returned with status " + response.getStatusCode());
    }
}
