package ml.chromaryu.IrunaReloaded.SQL;

import ml.chromaryu.IrunaReloaded.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by chroma on 16/06/22.
 */
public class SqlHandler {
    private String url, username, password;
    public Properties properties;
    boolean ismysql = false;
    Connection connection;
    public static String path = System.getProperty("user.dir");
    public SqlHandler() {
        try {
            //System.out.println(path);
            properties = new Properties();
            InputStream is = new FileInputStream(new File(path + "/sql.properties"));
            properties.load(is);
            if(properties.getProperty("sqltype").equalsIgnoreCase("mysql")) {
                ismysql = true;
                url = "jdbc:mysql://" + properties.getProperty("sql.url") + ":" + properties.getProperty("sql.port") + "/" + properties.getProperty("sql.db") + "?useUnicode=true&characterEncoding=utf-8";
                username = properties.getProperty("sql.user");
                password = properties.getProperty("sql.pass");
            } else {
                url = "jdbc:sqlite:" + path + "/" +properties.getProperty("sqlite.db") + ".db";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection open() throws SQLException {
        try{
            if(ismysql) {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(url,username,password);
            } else {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(url);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return connection;
    }
    public boolean maketables() {
        Connection connection = null;
        try {
            connection = open();
            if(ismysql) {
                PreparedStatement ps = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS Factoid" +
                                "(" +
                                "    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                                "    keyword VARCHAR(255) NOT NULL," +
                                "    message VARCHAR(255) NOT NULL," +
                                "    hostmask VARCHAR(255) NOT NULL," +
                                "    nickname VARCHAR(255) NOT NULL" +
                                ");"
                        //
                );
                ps.execute();
                ps = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS Permission" +
                                "(" +
                                "    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                                "    hostmask VARCHAR(256) NOT NULL," +
                                "    nickname VARCHAR(256) NOT NULL," +
                                "    permissionlevel INT NOT NULL" +
                                ");"
                        //
                );
                ps.execute();
            } else {
                PreparedStatement ps = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS Factoid" +
                                "(" +
                                "    id INT PRIMARY KEY NOT NULL," +
                                "    keyword VARCHAR(255) NOT NULL," +
                                "    message VARCHAR(255) NOT NULL," +
                                "    hostmask VARCHAR(255) NOT NULL," +
                                "    nickname VARCHAR(255) NOT NULL" +
                                ");"
                        //
                );
                ps.execute();
                ps = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS Permission" +
                                "(" +
                                "    id INT PRIMARY KEY NOT NULL," +
                                "    hostmask VARCHAR(256) NOT NULL," +
                                "    nickname VARCHAR(256) NOT NULL," +
                                "    permissionlevel INT NOT NULL" +
                                ");"
                        //
                );
                ps.execute();
            }
            /*ps = connection.prepareStatement(
                    "CREATE UNIQUE INDEX unique_id ON Permission (id);"
            );
            ps.execute();
            ps = connection.prepareStatement(
                    "CREATE UNIQUE INDEX unique_id ON Factoid (id) ;"
            );
            ps.execute();*/
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(connection);
        }
        return true;
    }
    public String getFactoid(String keyword,String hostmask, String nickname) {
        Connection connection = null;
        String message = null;
        try {
            connection = open();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT message FROM Factoid WHERE keyword = ? OR hostmask = ? OR nickname = ?;"
            );
            ps.setString(1,keyword);
            ps.setString(2,hostmask);
            ps.setString(3,nickname);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                message = rs.getString("message");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
        return message;
    }
    public boolean setFactoid(String keyword,String message,String hostmask, String nickname) {
        Connection connection = null;
        try {
            connection = open();
            PreparedStatement ps = connection.prepareStatement(
              "INSERT INTO Factoid (keyword, message, hostmask, nickname) VALUES (?,?,?,?);"
            );
            ps.setString(1,keyword);
            ps.setString(2,message);
            ps.setString(3,hostmask);
            ps.setString(4,nickname);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(connection);
        }
        return true;
    }
    public boolean updateFactoid(String keyword,String message) {
        Connection connection = null;
        try {
            connection = open();
            PreparedStatement ps = connection.prepareStatement(
                "UPDATE Factoid SET message = ? WHERE keyword = ?"
            );
            ps.setString(2,keyword);
            ps.setString(1,message);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(connection);
        }
        return true;
    }
    public List getFactoidByNickname(String nickname) {
        Connection connection = null;
        List<Integer> list;
        try {
            connection = open();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT id FROM Factoid WHERE nickname = ?;"
            );
            ps.setString(1,nickname);
            ResultSet rs = ps.executeQuery();
            list = new ArrayList<>();
            while(rs.next()) {
                list.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(connection);
        }
        return list;
    }
    public boolean addPermissionLevel(String hostname,String nickname,int permlevel) {
        Connection connection = null;
        try {
            connection = open();
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Permission(hostmask, nickname, permissionlevel) VALUES (?,?,?)"
            );
            ps.setString(1,hostname);
            ps.setString(2,nickname);
            ps.setInt(3,permlevel);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(connection);
        }
        return true;
    }
    public boolean modifyPermissionLevel(String hostname,String nickname, int permlevel){
        Connection connection = null;
        try {
            connection = open();
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE Permission SET permissionlevel = ? WHERE hostmask = ? OR nickname = ?"
            );
            ps.setInt(1,permlevel);
            ps.setString(2,hostname);
            ps.setString(3,nickname);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(connection);
        }
        return true;
    }
    public int getPermissionLevel(String hostname,String nickname) {
        Connection connection = null;
        int permissionLevel = -1;
        try {
            connection = open();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT permissionlevel FROM Permission WHERE hostmask = ? OR nickname = ?"
            );
            ps.setString(1,hostname);
            ps.setString(2,nickname);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                permissionLevel = rs.getInt("permissionlevel");
        } catch (SQLException e) {
            e.printStackTrace();
            return -255; // Error Code
        } finally {
            close(connection);

        }
        return permissionLevel;
    }
}
