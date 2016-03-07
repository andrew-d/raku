package io.dunham.raku.viewmodel;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import io.dunham.raku.model.Tag;


@EqualsAndHashCode
@ToString
public class TagWithEmbeddedDocumentsVM {
    @Getter @Setter private long id;
    @Getter @Setter private String name;
    @Getter private Set<DocumentVM> documents;

    public TagWithEmbeddedDocumentsVM() {
    }

    public TagWithEmbeddedDocumentsVM(Tag t) {
        this.id = t.getId();
        this.name = t.getName();

        if (t.getDocuments() != null) {
            this.documents = t.getDocuments()
                .stream()
                .map(d -> new DocumentVM(d))
                .collect(Collectors.toSet());
        } else {
            this.documents = Sets.newHashSet();
        }
    }

    public static List<TagWithEmbeddedDocumentsVM> mapList(List<Tag> tags) {
        return tags.stream()
            .map(TagWithEmbeddedDocumentsVM::new)
            .collect(Collectors.toList());
    }
}
