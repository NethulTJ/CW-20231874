package com.mycompany.cw;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotSupportedException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<Throwable> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof ApiException apiException) {
            return build(apiException.getStatus(), apiException.getMessage());
        }

        if (exception instanceof NotSupportedException) {
            return build(Response.Status.UNSUPPORTED_MEDIA_TYPE, "Content-Type must be application/json.");
        }

        if (exception instanceof BadRequestException) {
            String message = exception.getMessage();
            if (message == null || message.isBlank() || message.contains("Error parsing entity")) {
                message = "The request body is invalid or malformed.";
            }
            return build(Response.Status.BAD_REQUEST, message);
        }

        if (exception instanceof WebApplicationException webException) {
            Response.Status status = Response.Status.fromStatusCode(webException.getResponse().getStatus());
            return build(status == null ? Response.Status.INTERNAL_SERVER_ERROR : status, webException.getMessage());
        }

        return build(Response.Status.INTERNAL_SERVER_ERROR, "An unexpected server error occurred.");
    }

    private Response build(Response.StatusType status, String message) {
        String path = uriInfo == null ? "" : "/" + uriInfo.getPath();
        CampusData.ErrorBody body = new CampusData.ErrorBody(status.getStatusCode(), status.getReasonPhrase(), message, path);
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(body)
                .build();
    }
}
