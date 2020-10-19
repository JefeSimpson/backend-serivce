package org.github.jefesimpson.backend.service.controller;

import io.javalin.http.Context;

public interface Controller<T>{
    void create(Context context);
    void getAll(Context context);
    void getById(Context context, int id);
    void updateById(Context context, int id);
    void deleteById(Context context, int id);
}
