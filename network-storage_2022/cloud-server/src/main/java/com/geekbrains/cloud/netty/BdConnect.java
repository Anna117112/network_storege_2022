package com.geekbrains.cloud.netty;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// для поюключения бд надо подключить dependency в pomo
public class BdConnect {
    private static Connection connection;
    // для выполения CRUD операций с бд
    private static Statement stm;
    public Statement getStmt() {
        return stm;
    }

    public BdConnect(){
        try {
            Class.forName("org.sqlite.JDBC");// загрузка класса в память
            connection =  DriverManager.getConnection("jdbc:sqlite:mdb.db"); // соединение с бд
            stm = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Невозможно подключиться к базе данных");
        }

    }

//    public static void main(String[] args) {
//        try {
//            connect();
//
//            } finally {
//            disconnect();
//        }
//
//    }

    private static void fillTable() throws SQLException {
        long time = System.currentTimeMillis();
        connection.setAutoCommit(false);
        for (int i = 1; i <= 50; i++) {
            // 1 BOB #1 100
            stm.executeUpdate(String.format("insert into students (name, score) values ('%s', %d);", "BOB #" + i, 100));
        }
        connection.commit();
        System.out.println("TIME: " + (System.currentTimeMillis() - time));
    }
    private static void createTable() throws SQLException {
        stm.executeUpdate("drop table if exists students;");
//        stm.executeUpdate("CREATE TABLE if not exists users (\n" +
//                "    id    INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
//                "    name  TEXT,\n" +
//                "    score INTEGER\n" +
//                ");;");
    }

    // закрываем соединение
    public static void disconnect(){
        //  проверяем на пустое занчение чтобы не поймать ошибку NullpointEcseption
        if (connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            }
    }
}
