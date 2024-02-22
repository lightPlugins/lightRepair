package de.lightplugins.repair.commands.main;

import de.lightplugins.repair.enums.MessagePath;
import de.lightplugins.repair.master.Main;
import de.lightplugins.repair.util.SubCommand;
import org.bukkit.entity.Player;

public class ReloadCommand extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "reload the kits and refresh the cache";
    }

    @Override
    public String getSyntax() {
        return "/lrepair reload";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        if(player.hasPermission("lightrepair.admin.command.reload")) {
            Main.kits.reloadConfig("kits.yml");
            Main.kits.reloadConfig("settings.yml");
            Main.kits.reloadConfig("messages.yml");
            Main.kitBuilder.reloadKits();
            Main.util.sendMessage(player, MessagePath.Reload.getPath());
            return true;
        }
        return false;
    }
}
