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

import io.dunham.raku.model.Document;


@EqualsAndHashCode
@ToString
public class DocumentWithTagIdsVM {
    @Getter @Setter private long id;
    @Getter @Setter private String name;
    @Getter @Setter private Set<FileVM> files;
    @Getter private Set<Long> tags;

    public DocumentWithTagIdsVM() {
    }

    public DocumentWithTagIdsVM(Document d) {
        this.id = d.getId();
        this.name = d.getName();

        if (d.getFiles() != null) {
            this.files = d.getFiles()
                .stream()
                .map(f -> new FileVM(f))
                .collect(Collectors.toSet());
        } else {
            this.files = Sets.newHashSet();
        }

        if (d.getTags() != null) {
            this.tags = d.getTags()
                .stream()
                .map(t -> t.getId())
                .collect(Collectors.toSet());
        } else {
            this.tags = Sets.newHashSet();
        }
    }

    public static List<DocumentWithTagIdsVM> mapList(List<Document> docs) {
        return docs.stream()
            .map(DocumentWithTagIdsVM::new)
            .collect(Collectors.toList());
    }
}
