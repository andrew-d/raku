package io.dunham.raku.model;

import java.util.Objects;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@EqualsAndHashCode
public class File {
    @Getter @Setter private long id;
    @Getter @Setter private String hash;
    @Getter @Setter private long size;
    @Getter @Setter private String filename;
    @Getter @Setter private String contentType;
    @Getter @Setter private Document document;

    public File() {
    }

    public File(String hash, long size, String filename, Document document) {
        this.hash = hash;
        this.size = size;
        this.filename = filename;
        this.document = document;
    }
}
