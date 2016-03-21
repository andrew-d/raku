package io.dunham.raku.helpers.pagination;

import com.google.common.base.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@EqualsAndHashCode
@ToString
public class PaginationParams {
    public static final long DEFAULT_OFFSET = 0;
    public static final long DEFAULT_LIMIT = 20;

    @Getter @Setter private long offset = DEFAULT_OFFSET;
    @Getter @Setter private long limit = DEFAULT_LIMIT;
    @Getter @Setter private Long total = null;

    public PaginationParams() {
    }

    public PaginationParams(long offset, long limit) {
        setOffset(offset);
        setLimit(limit);
    }

    public PaginationParams(long offset, long limit, Long total) {
        setOffset(offset);
        setLimit(limit);
        setTotal(total);
    }
}
