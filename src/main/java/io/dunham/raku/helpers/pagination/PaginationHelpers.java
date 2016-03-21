package io.dunham.raku.helpers.pagination;

import javax.ws.rs.container.ContainerRequestContext;


public class PaginationHelpers {
    private static final String PROPERTY_NAME = "paginationParams";

    public static void setParams(ContainerRequestContext ctx, PaginationParams params) {
        ctx.setProperty(PROPERTY_NAME, params);
    }

    public static PaginationParams getParams(ContainerRequestContext ctx) {
        return (PaginationParams)ctx.getProperty(PROPERTY_NAME);
    }
}
