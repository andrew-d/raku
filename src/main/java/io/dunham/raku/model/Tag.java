package io.dunham.raku.model;

import java.util.Objects;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@EqualsAndHashCode(exclude = {"documents"})
public class Tag {
    @Getter @Setter private long id;
    @Getter @Setter private String name;
    @Getter @Setter private Set<Document> documents;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }
}
