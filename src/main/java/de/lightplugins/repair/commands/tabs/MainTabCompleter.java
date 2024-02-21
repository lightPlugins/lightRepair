package de.lightplugins.repair.commands.tabs;

import de.lightplugins.repair.master.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainTabCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        Player player = (Player) sender;

        if(args.length == 1) {
            List<String> arguments = new ArrayList<>();

            if(player.hasPermission("lightrepair.admin.command.get")) {
                arguments.add("get");
            }

            return arguments;
        }

        if(args.length == 2) {
            List<String> arguments = new ArrayList<>();

            if(player.hasPermission("lightrepair.admin.command.get")) {
                Bukkit.getServer().getOnlinePlayers().forEach(p -> arguments.add(p.getName()));
            }

            return arguments;
        }

        if(args.length == 3) {
            List<String> arguments = new ArrayList<>();

            if(player.hasPermission("lightrepair.admin.command.get")) {
                arguments.addAll(Main.kitBuilder.getKitNames());
            }

            return arguments;
        }

        return null;
    }
}
