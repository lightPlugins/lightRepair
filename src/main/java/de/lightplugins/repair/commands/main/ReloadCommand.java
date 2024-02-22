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

        if(!player.hasPermission("lightrepair.admin.command.reload")) {
            Main.util.sendMessage(player, MessagePath.NoPermission.getPath());
            Main.util.playSoundOnFail(player);
            return false;
        }

        Main.kits.reloadConfig("kits.yml");
        Main.settings.reloadConfig("settings.yml");
        Main.messages.reloadConfig("messages.yml");
        Main.kitBuilder.reloadKits();
        Main.util.sendMessage(player, MessagePath.Reload.getPath());
        Main.util.playSoundOnSuccess(player);
        return true;

    }
}
