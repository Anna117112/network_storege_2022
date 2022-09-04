package com.geekbrains.cloud.netty;

import java.sql.*;

public class JdbcMainApp {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement psInsert;

    // CRUD

    public static void main(String[] args) {
        try {
            connect();
         //   dropAndCreateTable();
           // fillTable();
           // readExample();
            //stmt.executeUpdate("insert into users (name, score) values ('Bob', 31);");
             readExample();
            System.out.println(1);
            // prepareStatements();
            // preparedStatementExample();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private static void preparedStatementExample() throws SQLException {
        connection.setAutoCommit(false);
        for (int i = 1; i <= 50; i++) {
            // insert into students (name, score) values (?, ?);
            psInsert.setString(1, "BOB" + i);
            psInsert.setInt(2, 100);
            psInsert.executeUpdate();
        }
        connection.commit();
    }

    private static void prepareStatements() throws SQLException {
        psInsert = connection.prepareStatement("insert into students (name, score) values (?, ?);");
    }

    private static void batchExample() throws SQLException {
        connection.setAutoCommit(false);
        for (int i = 1; i <= 50; i++) {
            stmt.addBatch(String.format("insert into students (name, score) values ('%s', %d);", "BOB #" + i, 100));
        }
        stmt.executeBatch();
        connection.commit();
    }

    private static void dropAndCreateTable() throws SQLException {
       // stmt.executeUpdate("drop table if exists students;");
        stmt.executeUpdate("CREATE TABLE if not exists users (\n" +
                "    id    INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    name  TEXT,\n" +
                "    score INTEGER\n" +
                ");;");
    }

    private static void fillTable() throws SQLException {
        long time = System.currentTimeMillis();
        connection.setAutoCommit(false);
        for (int i = 1; i <= 50; i++) {
            // 1 BOB #1 100
            stmt.executeUpdate(String.format("insert into students (name, score) values ('%s', %d);", "BOB #" + i, 100));
        }
        connection.commit();
        System.out.println("TIME: " + (System.currentTimeMillis() - time));
    }

    private static void clearTableExample() throws SQLException {
        stmt.executeUpdate("delete from students;");
    }

    private static void deleteOneExample() throws SQLException {
        stmt.executeUpdate("delete from students where id = 5;");
    }

    private static void updateExample() throws SQLException {
        stmt.executeUpdate("update students set score = 100 where id > 0;");
    }

    private static void readExample() throws SQLException {
        try (ResultSet rs = stmt.executeQuery("select * from users where id > 1;")) {
            while (rs.next()) {
                //   id     name     score
                // > 1      Bob      90
                //   2      Jack     80
                System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getInt(3));
            }
        }
    }

    private static void insertExample() throws SQLException {
        stmt.executeUpdate("insert into students (name, score) values ('Max', 90);");
    }

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mdb.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Невозможно подключиться к БД");
        }
    }

    public static int getStudentScoreByName(String name) {
        // >
        // > 100

        // >
        // > __
        try (ResultSet rs = stmt.executeQuery("select score from students where name = '" + name + "';")) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void disconnect() {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (psInsert != null) {
                psInsert.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}













