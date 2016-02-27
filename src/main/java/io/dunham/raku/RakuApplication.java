package io.dunham.raku;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class RakuApplication extends Application<RakuConfiguration> {
    public static void main(String[] args) throws Exception {
        new RakuApplication().run(args);
    }

	// TODO: more resource classes here
/*
    private final HibernateBundle<RakuConfiguration> hibernateBundle =
            new HibernateBundle<RakuConfiguration>(Person.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(RakuConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };
*/

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
        // bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(RakuConfiguration configuration, Environment environment) {
        // final PersonDAO dao = new PersonDAO(hibernateBundle.getSessionFactory());
    }
}
