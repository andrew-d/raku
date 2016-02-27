package io.dunham.raku;

import java.util.concurrent.Callable;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.core.Document;
import io.dunham.raku.core.Tag;
import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;


public class RakuApplication extends Application<RakuConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RakuApplication.class);

    private final HibernateBundle<RakuConfiguration> hibernateBundle =
            new HibernateBundle<RakuConfiguration>(Document.class, Tag.class) {
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
    }

    @Override
    public void run(RakuConfiguration configuration, Environment environment) {
        final SessionFactory sf = hibernateBundle.getSessionFactory();

        final DocumentDAO docDao = new DocumentDAO(sf);
        final TagDAO tagDao = new TagDAO(sf);


        Tag tag1 = new Tag("foo");
        Tag tag2 = new Tag("bar");
        Tag tag3 = new Tag("asdf");

        try {
            withHibernate(sf, () -> {
                LOGGER.info("Creating tags...");
                tagDao.create(tag1);
                tagDao.create(tag2);
                tagDao.create(tag3);
                LOGGER.info("Tags created");
                return null;
            });

            withHibernate(sf, () -> {
                LOGGER.info("All tags:");
                for (Tag t : tagDao.findAll()) {
                    LOGGER.info(" - {}", t.getName());
                }
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T withHibernate(SessionFactory sf, Callable<T> c) throws Exception {
        Session session = sf.openSession();
        try {
            ManagedSessionContext.bind(session);
            Transaction tr = session.beginTransaction();

            try {
                T ret = c.call();
                tr.commit();

                return ret;
            } catch (Exception e) {
                tr.rollback();
                throw e;
            }
        } finally {
            session.close();
            ManagedSessionContext.unbind(sf);
        }
    }

    // This is the entry point that kicks things off.
    public static void main(String[] args) throws Exception {
        new RakuApplication().run(args);
    }
}
