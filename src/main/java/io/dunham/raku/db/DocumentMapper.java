package io.dunham.raku.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import io.dunham.raku.model.Document;


public class DocumentMapper implements ResultSetMapper<Document> {
    public Document map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Document(r.getString("name"), null, null);
    }
}
