package io.dunham.raku;

import org.hibernate.SessionFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.util.CAStore;


public class RakuModule extends AbstractModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(RakuModule.class);

    private final SessionFactory sessionFactory;
    private final RakuConfiguration config;

    public RakuModule(SessionFactory sessionFactory, RakuConfiguration config) {
        this.sessionFactory = sessionFactory;
        this.config = config;
    }

    @Provides
    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    @Provides
    public RakuConfiguration getConfig() {
        return this.config;
    }

    @Provides
    @Singleton
    public CAStore getCAStore() {
        return new CAStore(this.config.getFilesDir(), true);
    }

    @Override
    protected void configure() {
        // TODO: can use this
        /*
        Multibinder<ScheduledTask> taskMultibinder = Multibinder.newSetBinder(binder(), ScheduledTask.class);
        taskMultibinder.addBinding().to(OldStatusesCleanupTask.class);
        taskMultibinder.addBinding().to(OldEntriesCleanupTask.class);
        taskMultibinder.addBinding().to(OrphanedFeedsCleanupTask.class);
        taskMultibinder.addBinding().to(OrphanedContentsCleanupTask.class);
        */
    }
}
