package io.dunham.raku.services;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import io.dropwizard.lifecycle.Managed;
import org.flywaydb.core.Flyway;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.RakuConfiguration;


@Singleton
public class StartupService implements Managed {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartupService.class);

    private final RakuConfiguration config;
    private final DBI jdbi;

    @Inject
    public StartupService(RakuConfiguration config, DBI jdbi) {
        this.config = config;
        this.jdbi = jdbi;
    }

    @Override
    public void start() throws Exception {
        updateSchema();
        // TODO: could insert initial data here?
    }

    // Run Flyway migrations
    private void updateSchema() {
        try {
            jdbi.withHandle((Handle h) -> {
                final DatabaseMetaData md = h.getConnection().getMetaData();
                final Flyway flyway = new Flyway();
                flyway.setDataSource(md.getURL(), "sa", "sa");
                flyway.setValidateOnMigrate(true);

                // Do the migration.
                flyway.migrate();
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() throws Exception {

    }

    private static DataSource getDataSource() {
        // TODO: implement
        return null;
    }

}

