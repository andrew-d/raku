package io.dunham.raku.model;

import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import io.dunham.raku.filtering.DocumentWithTagsView;


@EqualsAndHashCode
@ToString
public class Document {
    @Getter @Setter private long id;

    @Getter @Setter
    @NotNull
    @Size(min = 1, max = 256)
    private String name;

    @Getter @Setter
    @DocumentWithTagsView
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Tag> tags;

    public Document() {
    }

    public Document(String name) {
        this.name = name;
    }
}
