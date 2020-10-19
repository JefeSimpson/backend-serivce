package org.github.jefesimpson.backend.service.controller;

import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import org.github.jefesimpson.backend.service.model.User;
import org.github.jefesimpson.backend.service.service.UserServiceManager;

public interface AuthorizationController<T> extends Controller<T>{
    UserServiceManager userServiceManager();

    default User sender(Context context) {
        if (!context.basicAuthCredentialsExist())
            return null;

        String login = context.basicAuthCredentials().getUsername();
        String password = context.basicAuthCredentials().getPassword();
        return userServiceManager().authenticate(login, password);
    }

    default User senderOrThrowUnauthorized(Context context) {
        User user = sender(context);
        if (user == null)
            throw new UnauthorizedResponse();
        return user;
    }
}
