package io.dunham.raku.model;

import java.util.Objects;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@EqualsAndHashCode
@ToString
public class Tag {
    @Getter @Setter private long id;
    @Getter @Setter private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }
}
