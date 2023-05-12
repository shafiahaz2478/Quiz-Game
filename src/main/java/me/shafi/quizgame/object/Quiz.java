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

public class Quiz implements Listener {
    String[] questions =    {
                                "Test 1",
                                "Test 2",
                                "Test 3",
                                "Test 4"
                            };
    String[][] options =    {
                                {"1" , "2" , "3"},
                                {"1" , "2" , "3"},
                                {"1" , "2" , "3"},
                                {"1" , "2" , "3"},
                            };

    String[] answers =    {
                            "A",
                            "B",
                            "C",
                            "B"
                        };

    int index;
    int correct_guesses = 0;
    int total_questions = questions.length;
    int result;
    int seconds=10;

    QuizGame plugin;
    Player player;

    BukkitTask task;

    public Quiz(Player player, QuizGame plugin){
        this.plugin = plugin;
        this.player = player;
        giveQuestion();



    }

    public void giveQuestion(){
        if(index >= total_questions){
            result();
            return;
        }
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            seconds--;

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(String.valueOf(seconds)));
            if(seconds<=0) {
                giveAnswer();
            }
        },0, 20);
        player.sendMessage("Question " + (index + 1));
        player.sendMessage(questions[index]);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){

        if(event.getPlayer() == player){
            onAnswerGet(event.getMessage());
            event.setCancelled(true);
        }
    }

    public void onAnswerGet(String guess){
        if(guess.equalsIgnoreCase(answers[index])){
            correct_guesses++;
        }
        giveAnswer();

    }

    public void giveAnswer(){
        this.task.cancel();
        player.sendMessage("Answer is option:" + answers[index]);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            seconds=10;
            index++;
            giveQuestion();

        }, 40);

    }

    public void result(){
        task.cancel();
        player.sendMessage("Your score is: " + correct_guesses);
        plugin.quizHashMap.remove(player);

    }

}
