package io.dunham.raku.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;


public interface UtilityDAO {
    @SqlUpdate("BACKUP TO :it")
    void backup(@Bind String location);
}
