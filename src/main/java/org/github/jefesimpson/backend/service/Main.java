package org.github.jefesimpson.backend.service;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.javalin.Javalin;
import org.github.jefesimpson.backend.service.configuration.DatabaseUtils;
import org.github.jefesimpson.backend.service.configuration.ObjectMapperFactory;
import org.github.jefesimpson.backend.service.configuration.UserPermissionObjectMapperFactory;
import org.github.jefesimpson.backend.service.controller.BlogController;
import org.github.jefesimpson.backend.service.controller.Controller;
import org.github.jefesimpson.backend.service.controller.UserController;
import org.github.jefesimpson.backend.service.controller.VipAppController;
import org.github.jefesimpson.backend.service.json.deserializer.BlogDeserializer;
import org.github.jefesimpson.backend.service.json.deserializer.UserDeserializer;
import org.github.jefesimpson.backend.service.json.serializer.UserSerializer;
import org.github.jefesimpson.backend.service.model.Blog;
import org.github.jefesimpson.backend.service.model.User;
import org.github.jefesimpson.backend.service.model.VipApp;
import org.github.jefesimpson.backend.service.permission.Permission;
import org.github.jefesimpson.backend.service.permission.UserPermission;
import org.github.jefesimpson.backend.service.service.*;

import java.util.HashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.prefer405over404 = true;
            javalinConfig.showJavalinBanner = false;
            javalinConfig.enableDevLogging();
            javalinConfig.enableCorsForAllOrigins();
            javalinConfig.defaultContentType = "application/json";
        });


        SimpleModule module = new SimpleModule();
        module.addDeserializer(User.class, new UserDeserializer())
                .addDeserializer(Blog.class, new BlogDeserializer())
                .addSerializer(User.class, new UserSerializer());

        Map<Permission, Module> permissionModuleMap = new HashMap<>();
        permissionModuleMap.put(UserPermission.CREATE, module);
        permissionModuleMap.put(UserPermission.READ, module);
        permissionModuleMap.put(UserPermission.UPDATE, module);
        ObjectMapperFactory mapperFactory = new UserPermissionObjectMapperFactory(permissionModuleMap);

        Service<Blog> blogService = new BlogService();
        Service<VipApp> vipAppService = new VipAppService();
        UserService userService = new UserService();
        Controller<Blog> blogController = new BlogController(blogService, userService, mapperFactory);
        Controller<VipApp> vipAppController = new VipAppController(vipAppService, userService, mapperFactory);
        Controller<User> userController = new UserController(userService, mapperFactory);

        app.routes(() -> {
            path("users", () -> {
                get(userController::getAll);
                post(userController::create);
                path(":id", () -> {
                    get(ctx -> userController.getById(ctx,ctx.pathParam(":id", Integer.class).get()));
                    patch(ctx -> userController.updateById(ctx, ctx.pathParam(":id", Integer.class).get()));
                    delete(ctx -> userController.deleteById(ctx, ctx.pathParam(":id", Integer.class).get()));
                });
            });
            path("blogs", () -> {
                get(blogController::getAll);
                post(blogController::create);
                path(":id", () -> {
                    get(ctx -> blogController.getById(ctx, ctx.pathParam(":id", Integer.class).get()));
                    patch(ctx -> blogController.updateById(ctx, ctx.pathParam(":id", Integer.class).get()));
                    delete(ctx -> blogController.deleteById(ctx, ctx.pathParam(":id", Integer.class).get()));
                });
            });
            path("applications", () -> {
                get(vipAppController::getAll);
                post(vipAppController::create);
                path(":id", () -> {
                    delete(ctx -> vipAppController.deleteById(ctx, ctx.pathParam(":id", Integer.class).get()));
                });
            });
        });

        app.start(9950);
    }
}
