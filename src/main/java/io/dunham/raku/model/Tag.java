package io.dunham.raku.model;

import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@EqualsAndHashCode
@ToString
public class Tag {
    @Getter @Setter private long id;

    @Getter @Setter
    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }
}
