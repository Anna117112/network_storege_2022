package com.geekbrains.cloud.netty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DbAuthenticationProvider {
    private BdConnect dbConnection;

    public void init() {
        dbConnection = new BdConnect();
    }

    public String getNicknameByLoginAndPassword(String login, String password) {
        String query = String.format("select * from users where name = '%s' and score = '%s';", login, password);
        try (ResultSet rs = dbConnection.getStmt().executeQuery(query)) {
            while (rs.next()) {
                System.out.println(rs.getString(2));
                return (rs.getString(2));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return " Логин и пароль введены неверно ";
    }
}
