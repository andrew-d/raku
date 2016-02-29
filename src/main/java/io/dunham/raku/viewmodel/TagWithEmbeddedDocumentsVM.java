package io.dunham.raku.viewmodel;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.dunham.raku.model.Tag;


public class TagWithEmbeddedDocumentsVM {
    private long id;
    private String name;
    private Set<DocumentVM> documents;

    public TagWithEmbeddedDocumentsVM() {
    }

    public TagWithEmbeddedDocumentsVM(Tag t) {
        this.id = t.getId();
        this.name = t.getName();
        this.documents = t.getDocuments()
            .stream()
            .map(d -> new DocumentVM(d))
            .collect(Collectors.toSet());
    }

    public static List<TagWithEmbeddedDocumentsVM> mapList(List<Tag> tags) {
        return tags.stream()
            .map(TagWithEmbeddedDocumentsVM::new)
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

    public Set<DocumentVM> getDocuments() {
        return this.documents;
    }

    // --------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;

        if (!(o instanceof TagWithEmbeddedDocumentsVM)) {
            return false;
        }

        final TagWithEmbeddedDocumentsVM that = (TagWithEmbeddedDocumentsVM) o;

        return this.id == that.id &&
            this.name == that.name &&
            Objects.equals(this.documents, that.documents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, documents);
    }
}
