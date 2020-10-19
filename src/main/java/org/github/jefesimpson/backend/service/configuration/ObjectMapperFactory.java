package org.github.jefesimpson.backend.service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.github.jefesimpson.backend.service.permission.Permission;

public interface ObjectMapperFactory {
    ObjectMapper objectMapper(Permission permission);
}
