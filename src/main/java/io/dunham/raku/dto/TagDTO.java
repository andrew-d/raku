package io.dunham.raku.dto;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.dunham.raku.model.Tag;


public class TagDTO {
    private long id;
    private String name;

    public TagDTO() {
    }

    public TagDTO(Tag t) {
        this.id = t.getId();
        this.name = t.getName();
    }

    public static List<TagDTO> mapList(List<Tag> tags) {
        return tags.stream()
            .map(TagDTO::new)
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

        if (!(o instanceof TagDTO)) {
            return false;
        }

        final TagDTO that = (TagDTO) o;

        return this.id == that.id && this.name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
