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
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.OptionalContainerFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.background.BackupDatabaseScheduledTask;
import io.dunham.raku.background.OrphanedFilesScheduledTask;
import io.dunham.raku.cli.AddTagCommand;
import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;
import io.dunham.raku.helpers.ManagedPeriodicTask;
import io.dunham.raku.helpers.pagination.PaginationParams;
import io.dunham.raku.helpers.pagination.PaginationParamsFactory;
import io.dunham.raku.helpers.pagination.PaginationResponseFilter;
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

        // Register custom providers.
        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(PaginationParamsFactory.class)
                    .to(PaginationParams.class)
                    .in(RequestScoped.class);
            }
        });
        environment.jersey().register(EntityFilteringFeature.class);

        // Register filters.
        environment.jersey().getResourceConfig().register(PaginationResponseFilter.class);

        // Register our resources
        environment.jersey().register(injector.getInstance(TagResource.class));
        environment.jersey().register(injector.getInstance(TagsResource.class));
        environment.jersey().register(injector.getInstance(DocumentResource.class));
        environment.jersey().register(injector.getInstance(DocumentsResource.class));

        // Database initialization and migration
        environment.lifecycle().manage(injector.getInstance(StartupService.class));

        // Periodic tasks
        final Managed orphanedFiles = new ManagedPeriodicTask(injector.getInstance(OrphanedFilesScheduledTask.class));
        environment.lifecycle().manage(orphanedFiles);

        final Managed backupTask = new ManagedPeriodicTask(injector.getInstance(BackupDatabaseScheduledTask.class));
        environment.lifecycle().manage(backupTask);
    }

    // This is the entry point that kicks things off.
    public static void main(String[] args) throws Exception {
        new RakuApplication().run(args);
    }
}
