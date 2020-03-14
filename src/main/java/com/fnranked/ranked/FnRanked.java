package com.fnranked.ranked;

import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class FnRanked {

    private static Logger logger = Logger.getLogger("Test");

    public void startBot() {
        logger.info("Initializing Fortnite Ranked Match Service");
    }
}
