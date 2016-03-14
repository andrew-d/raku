package io.dunham.raku.viewmodel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@EqualsAndHashCode
@ToString
public class TopLevelVM {
    @Getter @Setter private Object data;
    @Getter @Setter private Meta meta;

    public TopLevelVM() {
        this.meta = new Meta();
    }

    public TopLevelVM(Object o) {
        this.data = o;
        this.meta = new Meta();
    }

    public static TopLevelVM of(Object o) {
        return new TopLevelVM(o);
    }

    @EqualsAndHashCode
    @ToString
    public static class Meta {
        @Getter @Setter private long count;
    }
}
