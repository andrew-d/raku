package io.dunham.raku.model;

import java.util.Objects;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@EqualsAndHashCode(exclude = {"tags", "files"})
public class Document {
    @Getter @Setter private long id;
    @Getter @Setter private String name;
    @Getter @Setter private Set<Tag> tags;
    @Getter @Setter private Set<File> files;

    public Document() {
    }

    public Document(String name, Set<Tag> tags, Set<File> files) {
        this.name = name;
        this.tags = tags;
        this.files = files;
    }
}
