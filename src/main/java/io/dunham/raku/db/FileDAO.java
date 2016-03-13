package io.dunham.raku.db;

import java.util.List;

import com.google.common.base.Optional;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import io.dunham.raku.model.Document;
import io.dunham.raku.model.File;


@RegisterMapper(FileMapper.class)
public interface FileDAO {
    @SqlQuery("SELECT * FROM files WHERE file_id = :it")
    @SingleValueResult
    Optional<File> findById(@Bind long id);

    @SqlQuery("SELECT * FROM files OFFSET :offset LIMIT :limit")
    List<File> findAll(@Bind("offset") long offset,
                       @Bind("limit") long limit);

    @SqlQuery("SELECT * FROM files WHERE hash = :it")
    @SingleValueResult
    Optional<File> findByHash(String hash);

    @SqlQuery("SELECT * FROM files WHERE document_id = :id")
    List<File> findByDocument(@BindBean Document doc);

    @SqlQuery("SELECT * FROM files WHERE document_id = :d.id AND hash = :hash")
    @SingleValueResult
    Optional<File> findByDocumentAndHash(@BindBean("d") Document doc, @Bind("hash") String hash);

    @SqlUpdate("INSERT INTO files (hash, size, filename, content_type, document_id) "
             + "VALUES (:hash, :size, :filename, :contentType, :documentId)")
    @GetGeneratedKeys
    long save(@BindBean File file);
}
