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

import io.dunham.raku.filtering.TagWithDocumentsView;


@EqualsAndHashCode
@ToString
public class Tag {
    @Getter @Setter private long id;

    @Getter @Setter
    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @Getter @Setter
    @TagWithDocumentsView
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Document> documents;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }
}
