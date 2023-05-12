package me.shafi.quizgame.commands;

import me.shafi.quizgame.QuizGame;
import me.shafi.quizgame.object.Quiz;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuizCommand implements CommandExecutor {
    QuizGame plugin;

    public QuizCommand(QuizGame plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) {
        if(sender instanceof Player p) {
            if (args[0].equalsIgnoreCase("start")) {
                Quiz quiz = new Quiz(p, plugin);
                plugin.getServer().getPluginManager().registerEvents(quiz , plugin);
                plugin.quizHashMap.put(p, quiz);
            } else if (args[0].equalsIgnoreCase("ans")) {
                Quiz quiz = plugin.quizHashMap.get(p);
                quiz.onAnswerGet(args[1]);
            }
        }
        return false;
    }
}
