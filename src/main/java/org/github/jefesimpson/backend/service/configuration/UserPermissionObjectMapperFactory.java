package org.github.jefesimpson.backend.service.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.github.jefesimpson.backend.service.permission.Permission;

import java.util.Map;

public class UserPermissionObjectMapperFactory implements ObjectMapperFactory {
    private Map<Permission, Module> permissions;

    public UserPermissionObjectMapperFactory() {
    }

    public UserPermissionObjectMapperFactory(Map<Permission, Module> permissions) {
        this.permissions = permissions;
    }

    public Map<Permission, Module> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<Permission, Module> permissions) {
        this.permissions = permissions;
    }

    @Override
    public ObjectMapper objectMapper(Permission permission) {
        ObjectMapper mapper = new ObjectMapper();
        Module module = permissions.get(permission);
        mapper.registerModule(module)
                .registerModule(new JavaTimeModule());
        return mapper;
    }
}
