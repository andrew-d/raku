package io.dunham.raku;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;
import io.dunham.raku.db.FileDAO;
import io.dunham.raku.util.CAStore;


public class RakuModule extends AbstractModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(RakuModule.class);

    private final DBI jdbi;
    private final RakuConfiguration config;

    public RakuModule(DBI jdbi, RakuConfiguration config) {
        this.jdbi = jdbi;
        this.config = config;
    }

    @Provides
    public DBI getSessionFactory() {
        return this.jdbi;
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

    @Provides
    public DocumentDAO provideDocumentDAO() {
        return jdbi.onDemand(DocumentDAO.class);
    }

    @Provides
    public TagDAO provideTagDAO() {
        return jdbi.onDemand(TagDAO.class);
    }

    @Provides
    public FileDAO provideFileDAO() {
        return jdbi.onDemand(FileDAO.class);
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
