package org.github.jefesimpson.backend.service.service;

import org.github.jefesimpson.backend.service.permission.Permission;
import org.github.jefesimpson.backend.service.model.Blog;
import org.github.jefesimpson.backend.service.model.User;
import org.github.jefesimpson.backend.service.model.VipApp;

import java.util.Set;

public interface UserServiceManager extends Service<User>{
    User authenticate(String login, String password);
    boolean loginExists(String login);
    User findByLogin(String login);
    Set<Permission> permissionsFor(User user, Blog blog);
    Set<Permission> permissionsFor(User user, User anotherUser);
    Set<Permission> permissionsFor(User user, VipApp vipApp);
}
