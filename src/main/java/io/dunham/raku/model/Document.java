package io.dunham.raku.model;

import java.util.Objects;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@EqualsAndHashCode
@ToString
public class Document {
    @Getter @Setter private long id;
    @Getter @Setter private String name;

    public Document() {
    }

    public Document(String name) {
        this.name = name;
    }
}
