CREATE TABLE tags (
     tag_id IDENTITY NOT NULL
    ,name   VARCHAR(100) NOT NULL

    ,PRIMARY KEY (tag_id)
    ,UNIQUE (name)
);

CREATE TABLE documents (
     document_id IDENTITY NOT NULL
    ,name        VARCHAR(256) NOT NULL

    ,PRIMARY KEY (document_id)
);

CREATE TABLE files (
     file_id      IDENTITY NOT NULL
    ,hash         VARCHAR(64) NOT NULL
    ,size         BIGINT NOT NULL
    ,filename     VARCHAR(256) NOT NULL
    ,content_type VARCHAR(256)
    ,document_id  BIGINT NOT NULL

    ,PRIMARY KEY (file_id)
    ,UNIQUE (hash)
    ,FOREIGN KEY (document_id) REFERENCES documents(document_id)
);

CREATE TABLE document_tags (
     tag_id      BIGINT NOT NULL
    ,document_id BIGINT NOT NULL

    ,PRIMARY KEY (document_id, tag_id)
);
