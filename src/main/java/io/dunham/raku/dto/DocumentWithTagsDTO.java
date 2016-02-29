package io.dunham.raku.dto;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.dunham.raku.model.Document;


public class DocumentWithTagsDTO {
    private long id;
    private String name;
    private Set<TagDTO> tags;

    public DocumentWithTagsDTO() {
    }

    public DocumentWithTagsDTO(Document d) {
        this.id = d.getId();
        this.name = d.getName();
        this.tags = d.getTags()
            .stream()
            .map(t -> new TagDTO(t))
            .collect(Collectors.toSet());
    }

    public static List<DocumentWithTagsDTO> mapList(List<Document> docs) {
        return docs.stream()
            .map(DocumentWithTagsDTO::new)
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

    public Set<TagDTO> getTags() {
        return this.tags;
    }

    // --------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;

        if (!(o instanceof DocumentWithTagsDTO)) {
            return false;
        }

        final DocumentWithTagsDTO that = (DocumentWithTagsDTO) o;

        return this.id == that.id &&
            this.name == that.name &&
            Objects.equals(this.tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tags);
    }
}
