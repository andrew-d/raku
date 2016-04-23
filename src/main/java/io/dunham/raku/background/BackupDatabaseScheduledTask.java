package io.dunham.raku.background;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import com.google.common.util.concurrent.AbstractScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.RakuConfiguration;;
import io.dunham.raku.db.UtilityDAO;


public class BackupDatabaseScheduledTask extends AbstractScheduledService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackupDatabaseScheduledTask.class);

    private final RakuConfiguration config;
    private final UtilityDAO dao;

    @Inject
    public BackupDatabaseScheduledTask(RakuConfiguration config, UtilityDAO dao) {
        this.config = config;
        this.dao = dao;
    }

    @Override
    protected void runOneIteration() throws Exception {
        final Path backupDir = this.config.getBackupDir();
        if (backupDir == null) {
            LOGGER.info("Skipping daily backup since no directory was given");
            return;
        }

        LOGGER.info("Backing up database to directory: {}");
        try {
            final Date currentTime = new Date();
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

            final String fileName = "raku-backup-" + df.format(currentTime) + ".zip";
            final Path outputPath = backupDir.resolve(fileName);

            this.dao.backup(outputPath.toString());
            LOGGER.info("Successfully backed up database to: {}", outputPath.toString());
        } catch (final InvalidPathException e) {
            LOGGER.error("Could not resolve backup path: {}", e);
        }
    }

    @Override
    protected AbstractScheduledService.Scheduler scheduler() {
        return AbstractScheduledService.Scheduler.newFixedRateSchedule(0, 24, TimeUnit.HOURS);
    }
}

