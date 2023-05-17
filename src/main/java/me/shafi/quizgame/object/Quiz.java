package me.shafi.quizgame.object;

import me.shafi.quizgame.QuizGame;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Quiz implements Listener {
    List<String> questions = new ArrayList<>() ;

    List<List<String>> options = new ArrayList<>();


    List<String> answers = new ArrayList<>() ;

    String category;

    int index;
    public int correct_guesses = 0;
    int total_questions ;
    int result;
    int seconds=10;

    QuizGame plugin;
    Player player;

    BukkitTask task;

    public Quiz(Player player, QuizGame plugin , String category){
        this.plugin = plugin;
        this.player = player;
        this.category = category;



        player.sendMessage(category);
        plugin.getConfig().getConfigurationSection("quiz." + category).getKeys(false).forEach(this::addQuestion
                );


        giveQuestion();

    }

    public void addQuestion(String question){

        questions.add(question);
        total_questions++;

        String answer = plugin.getConfig().getString("quiz." + category + "." + question + ".answer");
        List<String> option = plugin.getConfig().getStringList("quiz." + category + "." + question + ".options");

        Random random = new Random();

        for (int j = 3 - option.size(); j > 0 ; j--) {
            option.remove(random.nextInt(option.size()));
        }

        option.add(answer);
        Collections.shuffle(option);
        int answer_index = option.indexOf(answer);

        switch (answer_index) {
            case 0 -> answers.add("A");
            case 1 -> answers.add("B");
            case 2 -> answers.add("C");
            case 3 -> answers.add("D");
        }

        options.add(option);

    }

    public void giveQuestion(){
        if(index >= total_questions){
            player.sendMessage("§a(!) §6You have finished the quiz. please wait for admin to end the quiz to get the result");
            task.cancel();
            return;
        }
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            seconds--;

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(String.valueOf(seconds)));
            if(seconds<=0) {
                giveAnswer();
            }
        },0, 20);
        player.sendMessage("§aQuestion §6" + (index + 1));
        player.sendMessage("§d" + questions.get(index));
        player.sendMessage("§aA: §6" + options.get(index).get(0));
        player.sendMessage("§aB: §6" + options.get(index).get(1));
        player.sendMessage("§aC: §6" + options.get(index).get(2));
        player.sendMessage("§aD: §6" + options.get(index).get(3));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){

        if(event.getPlayer() == player){
            onAnswerGet(event.getMessage());
            event.setCancelled(true);
        }
    }

    public void onAnswerGet(String guess){
        if(guess.equalsIgnoreCase(answers.get(index))){
            correct_guesses++;
        }
        player.sendMessage("§6You choosed option:" + guess);
        giveAnswer();

    }

    public void giveAnswer(){
        this.task.cancel();
        seconds = 10;

        index++;
        giveQuestion();


    }

    public void result(){
        task.cancel();
        player.sendMessage("§aYour score is: §6" + correct_guesses);

    }

}
