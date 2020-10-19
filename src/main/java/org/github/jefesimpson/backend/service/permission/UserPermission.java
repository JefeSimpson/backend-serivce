package org.github.jefesimpson.backend.service.permission;

public enum UserPermission implements Permission {
    //admin
    DELETE,
    UPDATE,
    //vip,common
    READ,
    //user
    CREATE
}
