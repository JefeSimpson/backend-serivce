package org.github.jefesimpson.backend.service.service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.github.jefesimpson.backend.service.configuration.DatabaseUtils;
import org.github.jefesimpson.backend.service.model.Blog;

import java.sql.SQLException;

public class BlogService implements Service<Blog>{

    @Override
    public Dao<Blog, Integer> dao() {
        try {
            return DaoManager.createDao(DatabaseUtils.getSource(), Blog.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Blog dao exception");
        }
    }
}
