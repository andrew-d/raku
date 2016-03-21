package io.dunham.raku.helpers.pagination;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glassfish.hk2.api.Factory;


public class PaginationParamsFactory implements Factory<PaginationParams> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaginationParamsFactory.class);
    public static final String OFFSET_QUERY_PARAM = "offset";
    public static final String LIMIT_QUERY_PARAM = "limit";

    private final UriInfo uriInfo;

    @Inject
    public PaginationParamsFactory(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public PaginationParams provide() {
        final PaginationParams params = new PaginationParams();
        final MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();

        final String offset = queryParams.getFirst(OFFSET_QUERY_PARAM);
        if (offset != null) {
            params.setOffset(parsePositive(offset, "offset"));
        }

        final String limit = queryParams.getFirst(LIMIT_QUERY_PARAM);
        if (limit != null) {
            params.setLimit(parsePositive(limit, "limit"));
        }

        return params;
    }

    private Long parsePositive(String value, String name) {
        Long ret;

        try {
            ret = Long.parseLong(value);
        } catch (final NumberFormatException e) {
            LOGGER.debug("Error parsing " + name + " from: {}", value);
            throw new PaginationException("Error parsing " + name + " from supplied value: " + value);
        }

        if (ret < 0) {
            LOGGER.debug("Value for " + name + " is less than 0: {}", value);
            throw new PaginationException("Value for " + name + " cannot be less than 0: " + value);
        }

        return ret;
    }

    @Override
    public void dispose(PaginationParams t) { /* Do nothing */ }
}
