package io.dunham.raku;

import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.OptionalContainerFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.cli.AddTagCommand;
import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;
import io.dunham.raku.resources.DocumentResource;
import io.dunham.raku.resources.DocumentsResource;
import io.dunham.raku.resources.TagResource;
import io.dunham.raku.resources.TagsResource;
import io.dunham.raku.services.StartupService;


public class RakuApplication extends Application<RakuConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RakuApplication.class);

    @Override
    public String getName() {
        return "raku";
    }

    @Override
    public void initialize(Bootstrap<RakuConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(new AssetsBundle("/webapp/", "/", "index.html"));
        bootstrap.addBundle(new FlywayBundle<RakuConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(RakuConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }

            @Override
            public FlywayFactory getFlywayFactory(RakuConfiguration configuration) {
                return configuration.getFlywayFactory();
            }
        });
        bootstrap.addBundle(new MultiPartBundle());
        bootstrap.addBundle(new DBIExceptionsBundle());

        // Add commands
        // bootstrap.addCommand(new AddTagCommand(this, hibernateBundle));
    }

    @Override
    public void run(RakuConfiguration configuration, Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "embedded_h2");
        // jdbi.registerContainerFactory(new OptionalContainerFactory());

        // Guice init
        Injector injector = Guice.createInjector(new RakuModule(
            jdbi,
            configuration
        ));

        // Tell Jersey to serve things under /api/ - this is the same as writing:
        //
        //      server:
        //          rootPath: /api/
        //
        // in config.yml.  However, we do it here since the rest of the
        // application depends on this, and it shouldn't be user-configurable.
        ((DefaultServerFactory) configuration.getServerFactory()).setJerseyRootPath("/api/*");

        // Register our resources
        environment.jersey().register(injector.getInstance(TagResource.class));
        environment.jersey().register(injector.getInstance(TagsResource.class));
        environment.jersey().register(injector.getInstance(DocumentResource.class));
        environment.jersey().register(injector.getInstance(DocumentsResource.class));

        // Database initialization and migration
        environment.lifecycle().manage(injector.getInstance(StartupService.class));
    }

    // This is the entry point that kicks things off.
    public static void main(String[] args) throws Exception {
        new RakuApplication().run(args);
    }
}
