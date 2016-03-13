package io.dunham.raku.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import io.dunham.raku.model.Tag;


public class TagMapper implements ResultSetMapper<Tag> {
    public Tag map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        final Tag t = new Tag();
        t.setId(r.getLong("tag_id"));
        t.setName(r.getString("name"));

        return t;
    }
}
