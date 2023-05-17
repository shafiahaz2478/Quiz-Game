package me.shafi.quizgame;

import me.shafi.quizgame.Tabcompleter.QuizTabCompleter;
import me.shafi.quizgame.commands.QuizCommand;
import me.shafi.quizgame.data.*;
import me.shafi.quizgame.manager.QuizManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class QuizGame extends JavaPlugin {

    public QuizManager quizManager;

    public MariaDB mariaDB;
    public SQLGetter data;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.mariaDB = new MariaDB(this.getConfig().getString("mariadb.host"),this.getConfig().getString("mariadb.port"),
                this.getConfig().getString("mariadb.username"),this.getConfig().getString("mariadb.password"),
                this.getConfig().getString("mariadb.database"));

        try {
            this.mariaDB.connect();
        } catch (SQLException e) {
            getLogger().warning("Database not connected");
        }
        if(this.mariaDB.isConnected()){
            getLogger().info("Database connected ");
            data = new SQLGetter(this);
            data.createTable();

        }

        getServer().getPluginCommand("quiz").setExecutor(new QuizCommand(this));
        getServer().getPluginCommand("quiz").setTabCompleter(new QuizTabCompleter(this));
        getLogger().info("Plugin has loaded");


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
