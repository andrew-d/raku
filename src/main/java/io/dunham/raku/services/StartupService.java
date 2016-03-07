package io.dunham.raku.services;

import java.sql.Connection;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.RakuConfiguration;
import io.dunham.raku.util.UnitOfWork;


@Singleton
public class StartupService implements Managed {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartupService.class);

    private final SessionFactory sessionFactory;
    private final RakuConfiguration config;

    @Inject
    public StartupService(SessionFactory sessionFactory, RakuConfiguration config) {
        this.sessionFactory = sessionFactory;
        this.config = config;
    }

    @Override
    public void start() throws Exception {
        updateSchema();
        // TODO: could insert initial data here using UnitOfWork?
    }

    // Run Flyway migrations
    private void updateSchema() {
        try {
            final DataSource dataSource = getDataSource(sessionFactory);
            final Flyway flyway = new Flyway();
            flyway.setDataSource(dataSource);
            flyway.setValidateOnMigrate(true);

            // Do the migration.
            flyway.migrate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() throws Exception {

    }

    private static DataSource getDataSource(SessionFactory sessionFactory) {
        if (sessionFactory instanceof SessionFactoryImpl) {
            ConnectionProvider cp = ((SessionFactoryImpl) sessionFactory).getConnectionProvider();
            if (cp instanceof DatasourceConnectionProviderImpl) {
                return ((DatasourceConnectionProviderImpl) cp).getDataSource();
            }
        }
        return null;
    }

}

