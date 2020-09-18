package services;

import config.EnvConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractDbService {

    private Connection connection;

    AbstractDbService() {
        this.connect();
    }

    private void connect() {
        try {
             this.connection = DriverManager
                    .getConnection(EnvConfig.DB_URL, EnvConfig.DB_USER, EnvConfig.DB_PASS);
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
