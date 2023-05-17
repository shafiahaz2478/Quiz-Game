package me.shafi.quizgame.data;

import me.shafi.quizgame.QuizGame;
import me.shafi.quizgame.manager.QuizManager;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLGetter {

    private final QuizGame plugin;


    public SQLGetter(QuizGame plugin){
        this.plugin = plugin;
    }
    public void createTable() {
        try {
            String query = "CREATE TABLE IF NOT EXISTS quizgame ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255) NOT NULL,"
                    + "uuid VARCHAR(36) NOT NULL,"
                    + "score INT NOT NULL,"
                    + "event_time TIMESTAMP NOT NULL"
                    + ")";
            Statement statement = plugin.mariaDB.getConnection().createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void storeQuizGameScore(String playerName, String uuid, int score, Timestamp eventTimestamp) {
        try {
            String query = "INSERT INTO quizgame (name, uuid, score, event_time) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = plugin.mariaDB.getConnection().prepareStatement(query);
            statement.setString(1, playerName);
            statement.setString(2, uuid);
            statement.setInt(3, score);
            statement.setTimestamp(4, eventTimestamp);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getLastQuizGameScores(Player player) {
        List<String> scores = new ArrayList<>();
        String playerName = player.getName();
        try  {
            String query = "SELECT score, event_time FROM quizgame WHERE name = ? ORDER BY event_time DESC LIMIT 10";
            PreparedStatement statement = plugin.mariaDB.getConnection().prepareStatement(query);
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int score = resultSet.getInt("score");
                Timestamp eventTimestamp = resultSet.getTimestamp("event_time");
                String formattedScore = "§a" + eventTimestamp + ":§6 scored: " + score;
                scores.add(formattedScore);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }

}
