package io.dunham.raku.services;

import java.sql.Connection;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import io.dropwizard.lifecycle.Managed;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.structure.DatabaseObject;
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

    // Run Liquibase migrations
    private void updateSchema() {
        try {
            Connection connection = null;
            try {
                Thread currentThread = Thread.currentThread();
                ClassLoader classLoader = currentThread.getContextClassLoader();
                ResourceAccessor accessor = new ClassLoaderResourceAccessor(classLoader);

                DataSource dataSource = getDataSource(sessionFactory);
                connection = dataSource.getConnection();
                JdbcConnection jdbcConnection = new JdbcConnection(connection);

                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);

                if (database instanceof PostgresDatabase) {
                    // Need to tell Postgres to not quote things:
                    //
                    //      https://liquibase.jira.com/browse/CORE-2059
                    //      http://stackoverflow.com/a/32072823
                    database = new PostgresDatabase() {
                        @Override
                        public String escapeObjectName(String objectName, Class<? extends DatabaseObject> objectType) {
                            return objectName;
                        }
                    };
                    database.setConnection(jdbcConnection);
                }

                Liquibase liq = new Liquibase("migrations.xml", accessor, database);
                liq.update("prod");
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
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

