package me.shafi.quizgame;

import me.shafi.quizgame.commands.QuizCommand;
import me.shafi.quizgame.object.Quiz;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class QuizGame extends JavaPlugin {

    public HashMap<Player, Quiz> quizHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginCommand("quiz").setExecutor(new QuizCommand(this));
        getLogger().info("Plugin has loaded");


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
