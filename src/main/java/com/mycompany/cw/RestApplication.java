package com.mycompany.cw;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class RestApplication extends ResourceConfig {
    public RestApplication() {
        register(ApiResource.class);
        register(ApiExceptionMapper.class);
        register(ApiLoggingFilter.class);
    }
}
