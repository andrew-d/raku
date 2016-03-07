package io.dunham.raku;

import java.nio.file.Path;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;


public class RakuConfiguration extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @NotNull
    private Path filesDir;

    @NotNull
    private FlywayFactory flywayFactory;

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }

    @JsonProperty("flyway")
    public FlywayFactory getFlywayFactory() {
        return flywayFactory;
    }

    @JsonProperty("flyway")
    public void setFlywayFactory(FlywayFactory factory) {
        this.flywayFactory = factory;
    }

    @JsonProperty
    public Path getFilesDir() {
        return filesDir;
    }

    @JsonProperty
    public void setFilesDir(Path dir) {
        this.filesDir = dir;
    }
}
