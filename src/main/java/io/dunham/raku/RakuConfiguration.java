package io.dunham.raku;

import java.nio.file.Path;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;
import lombok.Getter;
import lombok.Setter;


public class RakuConfiguration extends Configuration {
    @Valid
    @NotNull
    @Getter @Setter
    private Path databaseLocation;

    @Valid
    @NotNull
    @Getter @Setter
    private Path filesDir;

    @Valid
    @Getter @Setter
    private Path backupDir;

    private FlywayFactory flywayFactory = null;
    private Object flywayFactoryLock = new Object();

    private DataSourceFactory database = null;
    private Object databaseLock = new Object();

    public DataSourceFactory getDataSourceFactory() {
        synchronized(databaseLock) {
            if (database == null) {
                database = new DataSourceFactory();
                database.setDriverClass("org.h2.Driver");
                database.setUrl("jdbc:h2:" + getDatabaseLocation());
                database.setUser("sa");
                database.setPassword("sa");
            }
        }

        return database;
    }

    public FlywayFactory getFlywayFactory() {
        synchronized(flywayFactoryLock) {
            if (flywayFactory == null) {
                flywayFactory = new FlywayFactory();
                flywayFactory.setLocations(ImmutableList.of("db/migration"));
            }
        }

        return flywayFactory;
    }
}
