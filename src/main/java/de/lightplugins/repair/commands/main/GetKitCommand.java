package de.lightplugins.repair.commands.main;

import de.lightplugins.repair.enums.MessagePath;
import de.lightplugins.repair.master.Main;
import de.lightplugins.repair.util.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class GetKitCommand extends SubCommand {
    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescription() {
        return "get a repair kit";
    }

    @Override
    public String getSyntax() {
        return "/lrepair get <playername> <kitname>";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        if(args.length != 3) {
            Main.util.sendMessage(player, MessagePath.WrongCommand.getPath()
                    .replace("#command#", getSyntax()));
            Main.util.playSoundOnFail(player);
            return false;
        }

        if(!player.hasPermission("lightrepair.admin.command.get")) {
            Main.util.sendMessage(player, MessagePath.NoPermission.getPath());
            Main.util.playSoundOnFail(player);
            return false;
        }

        String playerName = args[1];
        String kitName = args[2];

        Player target = Bukkit.getPlayer(playerName);

        if(target == null) {
            Main.util.sendMessage(player, MessagePath.PlayerNotFound.getPath());
            Main.util.playSoundOnFail(player);
            return false;
        }

        ItemStack is = Main.kitBuilder.getKitByName(kitName);

        if(is.getType().equals(Material.STONE)) {
            Main.util.sendMessage(player, MessagePath.KitNotFound.getPath());
            Main.util.playSoundOnFail(player);
            return false;
        }

        if(Main.util.isInventoryEmpty(player)) {
            Main.util.sendMessage(player, MessagePath.GetKitSuccess.getPath()
                    .replace("#kit#", kitName));
            player.getInventory().addItem(is);
            Main.util.playSoundOnSuccess(player);
            return false;
        }

        if(player.getLocation().getWorld() == null) {
            throw new RuntimeException("Players world not found for dropping kit");
        }

        player.getLocation().getWorld().dropItemNaturally(player.getLocation(), is);
        Main.util.sendMessage(player, MessagePath.GetKitInvFull.getPath());
        Main.util.playSoundOnSuccess(player);

        return false;
    }
}
