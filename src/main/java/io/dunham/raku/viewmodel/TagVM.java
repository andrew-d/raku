package io.dunham.raku.viewmodel;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import io.dunham.raku.model.Document;
import io.dunham.raku.model.Tag;


@EqualsAndHashCode
@ToString
public class TagVM {
    @Getter @Setter private long id;
    @Getter @Setter private String name;

    @Getter @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<DocumentVM> documents;

    public TagVM() {
    }

    public TagVM(Tag t) {
        this.id = t.getId();
        this.name = t.getName();
    }

    public TagVM withDocuments(Collection<Document> docs) {
        this.documents = docs.stream()
            .map(DocumentVM::new)
            .collect(Collectors.toSet());
        return this;
    }

    public static TagVM of(Tag t) {
        return new TagVM(t);
    }

    public static List<TagVM> mapList(Collection<Tag> tags) {
        return tags.stream()
            .map(TagVM::new)
            .collect(Collectors.toList());
    }
}
