package io.dunham.raku.helpers.pagination;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;


public class PaginationResponseFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        final PaginationParams params = PaginationHelpers.getParams(requestContext);
        if (params != null) {
            responseContext.getHeaders().add("X-Pagination-Offset", params.getOffset());
            responseContext.getHeaders().add("X-Pagination-Limit", params.getLimit());

            if (params.getTotal() != null) {
                responseContext.getHeaders().add("X-Pagination-Total", params.getTotal());
            }
        }
    }
}
