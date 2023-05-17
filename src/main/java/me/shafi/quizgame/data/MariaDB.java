package me.shafi.quizgame.data;

import me.shafi.quizgame.QuizGame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDB {
    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    private Connection connection;

    public MariaDB(String host, String port, String username , String password, String database){
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        }catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MariaDB JDBC driver", e);
        }
    }
    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() throws SQLException {
        if(!isConnected()){
            connection = DriverManager.getConnection("jdbc:mariadb://" +
                            host + ":" + port + "/" + database,
                    username, password);
        }
    }
    public void disconnect(){
        if(isConnected()){
            try {
                connection.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
