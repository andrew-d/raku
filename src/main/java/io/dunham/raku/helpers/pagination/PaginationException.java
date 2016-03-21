package io.dunham.raku.helpers.pagination;

import javax.ws.rs.WebApplicationException;
import static javax.ws.rs.core.Response.Status;


public class PaginationException extends WebApplicationException {
	public PaginationException(String message) {
		super(message, Status.BAD_REQUEST);
	}

	public PaginationException(String message, Exception e) {
		super(message, e, Status.BAD_REQUEST);
	}
}
