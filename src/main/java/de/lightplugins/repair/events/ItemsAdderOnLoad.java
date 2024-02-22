package de.lightplugins.repair.events;

import de.lightplugins.repair.master.Main;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ItemsAdderOnLoad implements Listener {

    @EventHandler
    public void waitForItemsAdder(ItemsAdderLoadDataEvent event) {
        Bukkit.getConsoleSender().sendMessage(Main.consolePrefix + "§7Itemsadder successfully loaded in async.");
        Bukkit.getConsoleSender().sendMessage(Main.consolePrefix + "§7Register repair kits ...");
        Main.kitBuilder.reloadKits();
        Bukkit.getConsoleSender().sendMessage(Main.consolePrefix + "§7Register repair kits §asuccessfully");
    }
}
