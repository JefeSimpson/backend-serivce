package org.github.jefesimpson.backend.service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.InternalServerErrorResponse;
import org.github.jefesimpson.backend.service.configuration.Constants;
import org.github.jefesimpson.backend.service.configuration.ObjectMapperFactory;
import org.github.jefesimpson.backend.service.model.Blog;
import org.github.jefesimpson.backend.service.model.User;
import org.github.jefesimpson.backend.service.permission.UserPermission;
import org.github.jefesimpson.backend.service.service.Service;
import org.github.jefesimpson.backend.service.service.UserServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class BlogController implements AuthorizationController<Blog> {
    private Logger logger;
    private final Service<Blog> blogService;
    private final UserServiceManager userServiceManager;
    private final ObjectMapperFactory objectMapperFactory;

    public BlogController(Service<Blog> blogService, UserServiceManager userServiceManager, ObjectMapperFactory objectMapperFactory) {
        this.blogService = blogService;
        this.userServiceManager = userServiceManager;
        this.objectMapperFactory = objectMapperFactory;
    }

    public Service<Blog> getBlogService() {
        return blogService;
    }

    public UserServiceManager getUserServiceManager() {
        return userServiceManager;
    }

    public ObjectMapperFactory getObjectMapperFactory() {
        return objectMapperFactory;
    }

    @Override
    public UserServiceManager userServiceManager() {
        return userServiceManager();
    }

    @Override
    public void create(Context context) {
        try {
            User sender = senderOrThrowUnauthorized(context);
            Blog payload = objectMapperFactory.objectMapper(UserPermission.CREATE).readValue(context.body(), Blog.class);

            if (userServiceManager.permissionsFor(sender, payload).contains(UserPermission.CREATE)) {
                blogService.create(payload);
                context.status(Constants.CREATED_201);
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
            List<Blog> allowedPost = blogService.all()
                    .stream()
                    .filter(blog -> userServiceManager.permissionsFor(sender, blog).contains(UserPermission.READ))
                    .collect(Collectors.toList());
            context.result(objectMapperFactory.objectMapper(UserPermission.READ).writeValueAsString(allowedPost));
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
            Blog targetPost = blogService.findById(id);
            if (userServiceManager.permissionsFor(sender, targetPost).contains(UserPermission.READ)) {
                context.result(objectMapperFactory.objectMapper(UserPermission.READ).writeValueAsString(targetPost));

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
            Blog targetPost = blogService.findById(id);
            if (userServiceManager.permissionsFor(sender, targetPost).contains(UserPermission.UPDATE)) {
                Blog updated = objectMapperFactory.objectMapper(UserPermission.UPDATE).readValue(context.body(), Blog.class);
                updated.setId(id);
                blogService.update(updated);
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
        Blog target = blogService.findById(id);
        if (userServiceManager.permissionsFor(sender, target).contains(UserPermission.DELETE)) {
            blogService.deleteById(id);
            context.status(Constants.NO_CONTENT_204);
        } else {
            logger.error("Error occurred deleting records");
            context.status(Constants.INTERNAL_SERVER_ERROR_500);
        }

    }
}
