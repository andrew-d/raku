package io.dunham.raku.viewmodel;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.dunham.raku.model.Document;


public class DocumentWithEmbeddedTagsVM {
    private long id;
    private String name;
    private Set<TagVM> tags;

    public DocumentWithEmbeddedTagsVM() {
    }

    public DocumentWithEmbeddedTagsVM(Document d) {
        this.id = d.getId();
        this.name = d.getName();
        this.tags = d.getTags()
            .stream()
            .map(t -> new TagVM(t))
            .collect(Collectors.toSet());
    }

    public static List<DocumentWithEmbeddedTagsVM> mapList(List<Document> docs) {
        return docs.stream()
            .map(DocumentWithEmbeddedTagsVM::new)
            .collect(Collectors.toList());
    }

    // --------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TagVM> getTags() {
        return this.tags;
    }

    // --------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;

        if (!(o instanceof DocumentWithEmbeddedTagsVM)) {
            return false;
        }

        final DocumentWithEmbeddedTagsVM that = (DocumentWithEmbeddedTagsVM) o;

        return this.id == that.id &&
            Objects.equals(this.name, that.name) &&
            Objects.equals(this.tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tags);
    }
}
