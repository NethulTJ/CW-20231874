package com.smartcampus.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        Map<String, Object> body = new LinkedHashMap<String, Object>();
        body.put("status", 403);
        body.put("error", "Forbidden");
        body.put("message", exception.getMessage());
        body.put("path", uriInfo == null ? "" : "/" + uriInfo.getPath());
        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity(body)
                .build();
    }
}
