package me.shafi.quizgame.Tabcompleter;

import me.shafi.quizgame.QuizGame;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class QuizTabCompleter implements TabCompleter {
    QuizGame plugin ;

    public QuizTabCompleter(QuizGame plugin){
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String arg, String[] args) {
        List<String> argument = new ArrayList<>();
        List<String> result = new ArrayList<>();

        if(args.length == 1){
            if(sender.hasPermission("quiz.admin")){
                argument.add("start");
                argument.add("stop");
            }
            argument.add("join");
            argument.add("score");
            for(String a : argument){
                if(a.toLowerCase().startsWith(args[0].toLowerCase())){
                    result.add(a);
                }
            }
            return result;
        } else if (args.length == 2) {
            if(args[0].equalsIgnoreCase("start")){
                argument.addAll(plugin.getConfig().getConfigurationSection("quiz").getKeys(false));
            }else if (args[0].equalsIgnoreCase("score")){
                for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
                    argument.add(player.getName());
                }
            }
            for(String a : argument){
                if(a.toLowerCase().startsWith(args[1].toLowerCase())){
                    result.add(a);
                }
            }
            return result;
        }



        return null;
    }
}
