package me.shafi.quizgame.commands;

import me.shafi.quizgame.QuizGame;
import me.shafi.quizgame.manager.QuizManager;
import me.shafi.quizgame.object.Quiz;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class QuizCommand implements CommandExecutor {
    QuizGame plugin;

    public QuizCommand(QuizGame plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) {
        if(sender instanceof Player p) {
            List<String> helpmsg = new ArrayList<>();
            helpmsg.add("§6 Quiz Game Help");
            helpmsg.add("§a/quiz join - §6Join the current quiz event");
            helpmsg.add("§a/quiz score {player} - §6Get the player score if playername is not provided it will give your score");

            if(args.length >= 1) {
                if(p.hasPermission("quiz.admin")) {
                    helpmsg.add("§a/quiz start {category_name} - §6Start a quiz event category name is required ");
                    helpmsg.add("§a/quiz stop - §6Stop the current quiz event");
                    if (args[0].equalsIgnoreCase("start")) {
                        if (plugin.quizManager != null) {
                            p.sendMessage("§c(!) §rtheres already a quiz running quiz");
                            return false;
                        }

                        if (args.length == 2) {
                            plugin.quizManager = new QuizManager(plugin, args[1]);
                            return true;
                        }else {
                            p.sendMessage(helpmsg.toArray(String[]::new));
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("stop")) {
                        if (plugin.quizManager == null) {
                            p.sendMessage("§c(!) §rtheres no quiz event at the moment");
                            return false;
                        }

                        plugin.quizManager.endQuiz(p);
                        plugin.quizManager = null;
                    } else if (args[0].equalsIgnoreCase("help")) {
                        p.sendMessage(helpmsg.toArray(String[]::new));
                    } else {
                        p.sendMessage(helpmsg.toArray(String[]::new));
                    }
                }


                if (args[0].equalsIgnoreCase("join")) {
                    if (plugin.quizManager == null) {
                        p.sendMessage("§c(!) §rtheres no quiz event currently running");
                        return false;
                    }
                    if (plugin.quizManager.quizExist(p)) {
                        p.sendMessage("§c(!) §rYou are already in a quiz");
                        return false;
                    }
                    plugin.quizManager.joinQuiz(p);
                    return true;
                } else if (args[0].equalsIgnoreCase("score")) {
                    Player target = p;
                    if (args.length == 2) {
                        target = Bukkit.getPlayer(args[1]);
                    }
                    p.sendMessage(plugin.data.getLastQuizGameScores(target).toArray(String[]::new));
                }else {
                    p.sendMessage(helpmsg.toArray(String[]::new));
                }
            }else{
                p.sendMessage(helpmsg.toArray(String[]::new));
            }
        }else{
            sender.sendMessage("§c(!) §rYou have to be a player");
        }
        return false;
    }
}
