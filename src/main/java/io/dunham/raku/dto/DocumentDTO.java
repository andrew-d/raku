package io.dunham.raku.dto;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.dunham.raku.model.Document;


public class DocumentDTO {
    private long id;
    private String name;

    public DocumentDTO() {
    }

    public DocumentDTO(Document d) {
        this.id = d.getId();
        this.name = d.getName();
    }

    public static List<DocumentDTO> mapList(List<Document> docs) {
        return docs.stream()
            .map(DocumentDTO::new)
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

    // --------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;

        if (o instanceof DocumentDTO) {
            final DocumentDTO that = (DocumentDTO) o;

            return this.id == that.id && this.name == that.name;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
