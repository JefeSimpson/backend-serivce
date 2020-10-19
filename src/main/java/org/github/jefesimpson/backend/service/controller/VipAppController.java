package org.github.jefesimpson.backend.service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.MethodNotAllowedResponse;
import org.github.jefesimpson.backend.service.configuration.Constants;
import org.github.jefesimpson.backend.service.configuration.ObjectMapperFactory;
import org.github.jefesimpson.backend.service.model.User;
import org.github.jefesimpson.backend.service.model.VipApp;
import org.github.jefesimpson.backend.service.permission.UserPermission;
import org.github.jefesimpson.backend.service.service.Service;
import org.github.jefesimpson.backend.service.service.UserServiceManager;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VipAppController implements AuthorizationController<VipApp>{
    private Logger logger;
    private final Service<VipApp> vipAppService;
    private final UserServiceManager userServiceManager;
    private final ObjectMapperFactory objectMapperFactory;

    public VipAppController(Service<VipApp> vipAppService, UserServiceManager userServiceManager, ObjectMapperFactory objectMapperFactory) {
        this.vipAppService = vipAppService;
        this.userServiceManager = userServiceManager;
        this.objectMapperFactory = objectMapperFactory;
    }

    @Override
    public UserServiceManager userServiceManager() {
        return userServiceManager;
    }

    @Override
    public void create(Context context) {
        User sender = senderOrThrowUnauthorized(context);
        VipApp application = new VipApp(0, sender, LocalDate.now());

        if (userServiceManager.permissionsFor(sender, application).contains(UserPermission.CREATE)) {
            vipAppService.create(application);
            context.status(Constants.CREATED_201);
        } else {
            logger.error("error controller saving record");
            context.status(Constants.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Override
    public void getAll(Context context) {
        try {
            User sender = senderOrThrowUnauthorized(context);
            List<VipApp> result = vipAppService.all()
                    .stream()
                    .filter(VipApp -> userServiceManager.permissionsFor(sender, VipApp).contains(UserPermission.READ))
                    .collect(Collectors.toList());
            context.result(objectMapperFactory.objectMapper(UserPermission.READ).writeValueAsString(result));
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
            VipApp application = vipAppService.findById(id);

            if (userServiceManager.permissionsFor(sender, application).contains(UserPermission.READ)) {
                context.result(objectMapperFactory.objectMapper(UserPermission.READ).writeValueAsString(vipAppService));
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
        throw new MethodNotAllowedResponse("Update not allowed", Collections.emptyMap());
    }

    @Override
    public void deleteById(Context context, int id) {
        throw new MethodNotAllowedResponse("Delete not allowed", Collections.emptyMap());
    }
}
