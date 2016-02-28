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
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.cli.AddTagCommand;
import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;
import io.dunham.raku.resources.TagResource;
import io.dunham.raku.resources.TagsResource;


public class RakuApplication extends Application<RakuConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RakuApplication.class);

    private final HibernateBundle<RakuConfiguration> hibernateBundle =
            new ScanningHibernateBundle<RakuConfiguration>("io.dunham.raku.model") {
                @Override
                public DataSourceFactory getDataSourceFactory(RakuConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

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

        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<RakuConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(RakuConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);

        // Add commands
        bootstrap.addCommand(new AddTagCommand(this, hibernateBundle));
    }

    @Override
    public void run(RakuConfiguration configuration, Environment environment) {
        // guice init
        Injector injector = Guice.createInjector(new RakuModule(
            hibernateBundle.getSessionFactory(),
            configuration
        ));

        // Register our resources
        environment.jersey().register(injector.getInstance(TagResource.class));
        environment.jersey().register(injector.getInstance(TagsResource.class));
    }

    // This is the entry point that kicks things off.
    public static void main(String[] args) throws Exception {
        new RakuApplication().run(args);
    }
}
