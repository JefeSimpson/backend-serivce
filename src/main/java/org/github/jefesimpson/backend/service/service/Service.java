package org.github.jefesimpson.backend.service.service;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public interface Service <T> {
    Dao<T, Integer> dao();


    default List<T> all() {
        try {
            return dao().queryForAll();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("listAll exception");
        }
    }

    default T findById(int id) {
        try {
            return dao().queryForId(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("findById exception");

        }
    }

    default void create(T object) {
        try {
            dao().create(object);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("create exception");
        }
    }

    default void update(T object) {
        try {
            dao().update(object);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("update exception");
        }
    }


    default void deleteById(int id) {
        try {
            dao().deleteById(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("delete exception");
        }
    }




}
