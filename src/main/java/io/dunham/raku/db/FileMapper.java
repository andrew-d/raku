package io.dunham.raku.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import io.dunham.raku.model.File;


public class FileMapper implements ResultSetMapper<File> {
    public File map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        final File f = new File();

        f.setId(r.getLong("file_id"));
        f.setHash(r.getString("hash"));
        f.setSize(r.getLong("size"));
        f.setFilename(r.getString("filename"));
        f.setContentType(r.getString("content_type"));

        return f;
    }
}

