package org.github.jefesimpson.backend.service.service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.github.jefesimpson.backend.service.configuration.DatabaseUtils;
import org.github.jefesimpson.backend.service.model.VipApp;

import java.sql.SQLException;

public class VipAppService implements Service<VipApp> {
    @Override
    public Dao<VipApp, Integer> dao() {
        try {
            return DaoManager.createDao(DatabaseUtils.getSource(), VipApp.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("VipApp dao exception");
        }
    }
}
