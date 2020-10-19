package org.github.jefesimpson.backend.service.configuration;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.github.jefesimpson.backend.service.configuration.Constants;
import org.github.jefesimpson.backend.service.model.Blog;
import org.github.jefesimpson.backend.service.model.User;
import org.github.jefesimpson.backend.service.model.VipApp;

import java.sql.SQLException;

public class DatabaseUtils {

    private static ConnectionSource source;


    static{
        try {
            source = new JdbcConnectionSource(Constants.DB_PATH);

            TableUtils.createTableIfNotExists(source, User.class);
            TableUtils.createTableIfNotExists(source, Blog.class);
            TableUtils.createTableIfNotExists(source, VipApp.class);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database initialize exception");
        }
    }

    public static ConnectionSource getSource() {
        if (source == null)
            throw new RuntimeException("Connection incorrect exception");
        return source;
    }

    public DatabaseUtils(){}
}
