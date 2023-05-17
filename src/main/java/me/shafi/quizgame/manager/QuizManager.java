package me.shafi.quizgame.manager;

import me.shafi.quizgame.QuizGame;
import me.shafi.quizgame.object.Quiz;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class QuizManager {
    QuizGame plugin;
    String category;
    public HashMap<Player, Quiz> quizHashMap = new HashMap<>();

    public QuizManager(QuizGame plugin , String category){
        this.plugin = plugin;
        this.category = category;
        anounceQuiz();
    }

    public void joinQuiz(Player player){
        Quiz quiz =  new Quiz(player, plugin , category);
        plugin.getServer().getPluginManager().registerEvents(quiz, plugin);
        quizHashMap.put(player ,quiz);
    }

    public boolean quizExist(Player player){
        return quizHashMap.containsKey(player);
    }

    public void anounceQuiz(){
            Bukkit.broadcastMessage("§aA quiz has started");
            Bukkit.broadcastMessage("§aTopic: §6" + category);
            Bukkit.broadcastMessage("§aDo \"/quiz join\" to join");
    }

    public void endQuiz(Player player){
        HashMap<Player , Integer> scores = new HashMap<>();
        for (Map.Entry<Player , Quiz> entry : quizHashMap.entrySet()){
            entry.getValue().result();
            //TODO for getting top score
            scores.put(entry.getKey() , entry.getValue().correct_guesses);
            plugin.data.storeQuizGameScore(entry.getKey().getDisplayName() , entry.getKey().getUniqueId().toString() , entry.getValue().correct_guesses , new Timestamp(System.currentTimeMillis()));
        }
        scores = scores.entrySet()
                .stream()
                .sorted(Map.Entry.<Player, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        int i = 1;
        for(Map.Entry<Player , Integer> entry : scores.entrySet()){
            if(i >= 10){
                break;
            }
            player.sendMessage("§a"+i+". " + entry.getKey().getDisplayName() + " scored: " + entry.getValue());
            i++;
        }

        scores.clear();
        quizHashMap.clear();
    }

}
