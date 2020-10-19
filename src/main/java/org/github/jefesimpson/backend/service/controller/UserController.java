package org.github.jefesimpson.backend.service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.InternalServerErrorResponse;
import org.github.jefesimpson.backend.service.configuration.Constants;
import org.github.jefesimpson.backend.service.configuration.ObjectMapperFactory;
import org.github.jefesimpson.backend.service.model.User;
import org.github.jefesimpson.backend.service.permission.UserPermission;
import org.github.jefesimpson.backend.service.service.UserServiceManager;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class UserController implements AuthorizationController<User>{
    private Logger logger;
    private final UserServiceManager userServiceManager;
    private final ObjectMapperFactory objectMapperFactory;

    public UserController(UserServiceManager userServiceManager, ObjectMapperFactory objectMapperFactory) {
        this.userServiceManager = userServiceManager;
        this.objectMapperFactory = objectMapperFactory;
    }

    public UserServiceManager getUserServiceManager() {
        return userServiceManager;
    }

    public ObjectMapperFactory getObjectMapperFactory() {
        return objectMapperFactory;
    }

    @Override
    public UserServiceManager userServiceManager() {
        return null;
    }

    @Override
    public void create(Context context) {
        try {
            User sender = sender(context);
            User payload = objectMapperFactory.objectMapper(UserPermission.CREATE).readValue(context.body(), User.class);

            if (userServiceManager.permissionsFor(sender, payload).contains(UserPermission.CREATE)) {
                userServiceManager.create(payload);
                context.status(Constants.CREATED_201);
            } else {
                throw new ForbiddenResponse();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("error controller saving record");
            context.status(Constants.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Override
    public void getAll(Context context) {
        try {
            User sender = senderOrThrowUnauthorized(context);

            List<User> result = userServiceManager.all()
                    .stream()
                    .filter(user -> userServiceManager.permissionsFor(sender, user).contains(UserPermission.READ))
                    .collect(Collectors.toList());
            context.result(objectMapperFactory.objectMapper(UserPermission.CREATE).writeValueAsString(result));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("error controller listing records");
            context.status(Constants.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Override
    public void getById(Context context, int id) {
        try {
            User sender = senderOrThrowUnauthorized(context);
            User target = userServiceManager.findById(id);
            if (userServiceManager.permissionsFor(sender, target).contains(UserPermission.READ)) {
                context.result(objectMapperFactory.objectMapper(UserPermission.READ).writeValueAsString(target));
            } else {
                throw new ForbiddenResponse();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("error controller getting records");
            context.status(Constants.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Override
    public void updateById(Context context, int id) {
        try {
            User sender = senderOrThrowUnauthorized(context);
            User target = userServiceManager.findById(id);

            if (userServiceManager.permissionsFor(sender, target).contains(UserPermission.UPDATE)) {
                context.result(objectMapperFactory.objectMapper(UserPermission.UPDATE).writeValueAsString(target));
                User updated = objectMapperFactory.objectMapper(UserPermission.UPDATE).readValue(context.body(), User.class);
                updated.setId(id);
                userServiceManager.update(updated);
                context.result(objectMapperFactory.objectMapper(UserPermission.UPDATE).writeValueAsString(updated));
            } else {
                throw new ForbiddenResponse();
            }
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
            logger.error("error occurred getting records");
            context.status(Constants.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Override
    public void deleteById(Context context, int id) {
        User sender = senderOrThrowUnauthorized(context);
        User target = userServiceManager.findById(id);
        if (userServiceManager.permissionsFor(sender, target).contains(UserPermission.DELETE)) {
            userServiceManager.deleteById(id);
            context.status(Constants.NO_CONTENT_204);
        } else {
            logger.error("Error occurred deleting records");
            context.status(Constants.INTERNAL_SERVER_ERROR_500);
        }
    }
}
