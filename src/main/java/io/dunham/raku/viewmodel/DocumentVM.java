package io.dunham.raku.viewmodel;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import io.dunham.raku.model.Document;
import io.dunham.raku.model.File;
import io.dunham.raku.model.Tag;


@EqualsAndHashCode
@ToString
public class DocumentVM {
    @Getter @Setter private long id;
    @Getter @Setter private String name;

    @Getter @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<FileVM> files;

    @Getter @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<TagVM> tags;

    public DocumentVM() {
    }

    public DocumentVM(Document d) {
        this.id = d.getId();
        this.name = d.getName();
    }

    public DocumentVM withFiles(Collection<File> files) {
        this.files = files
            .stream()
            .map(FileVM::new)
            .collect(Collectors.toSet());
        return this;
    }

    public DocumentVM withTags(Collection<Tag> tags) {
        this.tags = tags
            .stream()
            .map(TagVM::new)
            .collect(Collectors.toSet());
        return this;
    }

    public static DocumentVM of(Document doc) {
        return new DocumentVM(doc);
    }

    public static List<DocumentVM> mapList(Collection<Document> docs) {
        return docs.stream()
            .map(DocumentVM::new)
            .collect(Collectors.toList());
    }
}
