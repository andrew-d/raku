package io.dunham.raku.background;

import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.AbstractScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrphanedFilesScheduledTask extends AbstractScheduledService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrphanedFilesScheduledTask.class);

    @Override
    protected void runOneIteration() throws Exception {
        // TODO: implement me
        LOGGER.info("Cleaning up orphaned files...");
    }

    @Override
    protected AbstractScheduledService.Scheduler scheduler() {
        return AbstractScheduledService.Scheduler.newFixedRateSchedule(0, 10, TimeUnit.MINUTES);
    }
}
