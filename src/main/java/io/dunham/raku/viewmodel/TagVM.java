package io.dunham.raku.viewmodel;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import io.dunham.raku.model.Tag;


@EqualsAndHashCode
@ToString
public class TagVM {
    @Getter @Setter private long id;
    @Getter @Setter private String name;

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
}
