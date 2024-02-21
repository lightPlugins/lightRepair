package de.lightplugins.repair.commands.main;

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
        return "lrepair get <playername> <kitname>";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        if(args.length != 3) {
            player.sendMessage("Please use /lrepair get <playername> <kitname>");
            return false;
        }

        if(!player.hasPermission("lightrepair.admin.command.get")) {
            player.sendMessage("You don't have permission to use this command");
            return false;
        }

        String playerName = args[1];
        String kitName = args[2];

        Player target = Bukkit.getPlayer(playerName);

        if(target == null) {
            player.sendMessage("Player not found");
            return false;
        }

        ItemStack is = Main.kitBuilder.getKitByName(kitName);

        if(is.getType().equals(Material.STONE)) {
            player.sendMessage("Kit not found");
            return false;
        }

        if(Main.util.isInventoryEmpty(player)) {
            player.sendMessage("You got the kit " + kitName + " in your inventory size: " + is.getAmount());
            player.getInventory().addItem(is);
            return false;
        }

        if(player.getLocation().getWorld() == null) {
            player.sendMessage("Players world not found for dropping kit");
            return false;
        }

        player.getLocation().getWorld().dropItemNaturally(player.getLocation(), is);

        return false;
    }
}
