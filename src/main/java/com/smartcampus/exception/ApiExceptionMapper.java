package com.smartcampus.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<Throwable> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException) {
            WebApplicationException webException = (WebApplicationException) exception;
            Response.StatusType status = webException.getResponse().getStatusInfo();
            return build(status.getStatusCode(), status.getReasonPhrase(), webException.getMessage());
        }

        return build(500, "Internal Server Error", "An unexpected server error occurred.");
    }

    private Response build(int status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<String, Object>();
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        body.put("path", uriInfo == null ? "" : "/" + uriInfo.getPath());

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(body)
                .build();
    }
}
