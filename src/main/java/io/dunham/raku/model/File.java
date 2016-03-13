package io.dunham.raku.model;

import java.util.Objects;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@EqualsAndHashCode
@ToString
public class File {
    @Getter @Setter private long id;
    @Getter @Setter private String hash;
    @Getter @Setter private long size;
    @Getter @Setter private String filename;
    @Getter @Setter private String contentType;
    @Getter @Setter private long documentId;

    public File() {
    }

    public File(String hash, long size, String filename, long documentId) {
        this.hash = hash;
        this.size = size;
        this.filename = filename;
        this.documentId = documentId;
    }
}
