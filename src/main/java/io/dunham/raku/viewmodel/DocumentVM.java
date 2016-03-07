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
public class DocumentVM {
    @Getter @Setter private long id;
    @Getter @Setter private String name;
    @Getter @Setter private Set<FileVM> files;

    public DocumentVM() {
    }

    public DocumentVM(Document d) {
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
    }

    public static List<DocumentVM> mapList(List<Document> docs) {
        return docs.stream()
            .map(DocumentVM::new)
            .collect(Collectors.toList());
    }
}
