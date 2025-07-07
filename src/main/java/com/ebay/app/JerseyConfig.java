package com.ebay.app;

import static org.springframework.core.annotation.AnnotationFilter.packages;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // Register the package containing your REST resources
        packages("com.ebay.app.resource");
    }
}