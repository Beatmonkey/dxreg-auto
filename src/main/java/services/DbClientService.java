package services;

import com.google.gson.JsonObject;
import model.db.DbClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbClientService extends AbstractDbService {


    public List<DbClient> getClientsByLogin(String login) {
        List<DbClient> result = new ArrayList<>();
        try {
            Statement statement = this.getConnection().createStatement();
            String query = "select * from principals where name like '" + login + "';";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                DbClient dbClient = new DbClient();
                dbClient.setId(rs.getInt("id"));
                dbClient.setDomain(rs.getString("domain"));
                dbClient.setName(rs.getString("name"));
                dbClient.setVersion(rs.getInt("version"));
                dbClient.setUser(rs.getBoolean("is_user"));
                dbClient.setStatus(rs.getString("status"));
                dbClient.setFullName(rs.getString("full_name"));
                dbClient.setEmail(rs.getString("email"));
                dbClient.setCreatedTime(rs.getTime("created_time"));
                dbClient.setCloseTime(rs.getInt("close_time"));
                dbClient.setExpirationTime(rs.getTime("expiration_time"));
                dbClient.setExtensions(rs.getString("extensions"));
                result.add(dbClient);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.printf("Failed.");
            return result;
        }

    }
}




