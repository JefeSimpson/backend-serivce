package org.github.jefesimpson.backend.service.service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import org.github.jefesimpson.backend.service.model.UserRole;
import org.github.jefesimpson.backend.service.permission.Permission;
import org.github.jefesimpson.backend.service.configuration.DatabaseUtils;
import org.github.jefesimpson.backend.service.model.Blog;
import org.github.jefesimpson.backend.service.model.User;
import org.github.jefesimpson.backend.service.model.VipApp;
import org.github.jefesimpson.backend.service.permission.UserPermission;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService implements UserServiceManager{
    @Override
    public User authenticate(String login, String password) {
        if (!loginExists(login))
            throw new RuntimeException("login doesn't exist");

        User user = findByLogin(login);
        if (BCrypt.checkpw(password, user.getPassword()))
            return user;
        else return null;
    }

    @Override
    public boolean loginExists(String login) {
        return findByLogin(login)!=null;
    }

    @Override
    public User findByLogin(String login) {
        try {
            QueryBuilder<User, Integer> queryBuilder = dao().queryBuilder();
            queryBuilder.where().eq("login", login);
            PreparedQuery<User> preparedQuery = queryBuilder.prepare();
            User user = dao().queryForFirst(preparedQuery);
            return user;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Base exception occurred");
        }
    }

    @Override
    public Set<Permission> permissionsFor(User user, Blog blog) {
        Set<Permission> permissions = new HashSet<>();
        if (user.getUserRole() == UserRole.ADMIN) {
            permissions.addAll(Arrays.asList(UserPermission.values()));
        } else if (user.getUserRole() == UserRole.VIP) {
            permissions.add(UserPermission.READ);
        } else if (user.getUserRole() == UserRole.COMMON) {
            if (!blog.isVipAccess()) {
                permissions.add(UserPermission.READ);
            }
        }
        return permissions;
    }

    @Override
    public Set<Permission> permissionsFor(User user, User anotherUser) {
        Set<Permission> permissions = new HashSet<>();
        if (user == null) {
            permissions.add(UserPermission.CREATE);
            return permissions;
        }
        if (user.getUserRole() == UserRole.ADMIN) {
            if (anotherUser.getUserRole() != UserRole.ADMIN) {
                permissions.add(UserPermission.DELETE);
            }
            permissions.addAll(Arrays.stream(UserPermission.values()).filter(permission -> permission != UserPermission.DELETE).collect(Collectors.toSet()));
        } else {
            permissions.add(UserPermission.READ);
        }
        return permissions;
    }

    @Override
    public Set<Permission> permissionsFor(User user, VipApp vipApp) {
        Set<Permission> permissions = new HashSet<>();
        if (user.getUserRole() == UserRole.COMMON) {
            permissions.add(UserPermission.CREATE);
        } else if (user.getUserRole() == UserRole.ADMIN) {
            permissions.add(UserPermission.READ);
        }
        return permissions;
    }

    @Override
    public Dao<User, Integer> dao() {
        try {
            return DaoManager.createDao(DatabaseUtils.getSource(), User.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("User dao exception");
        }
    }
}
