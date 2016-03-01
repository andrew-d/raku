package io.dunham.raku.viewmodel;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.dunham.raku.model.Tag;


public class TagVM {
    private long id;
    private String name;

    public TagVM() {
    }

    public TagVM(Tag t) {
        this.id = t.getId();
        this.name = t.getName();
    }

    public static List<TagVM> mapList(List<Tag> tags) {
        return tags.stream()
            .map(TagVM::new)
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

        if (!(o instanceof TagVM)) {
            return false;
        }

        final TagVM that = (TagVM) o;

        return this.id == that.id && Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
