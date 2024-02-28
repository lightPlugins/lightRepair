package de.lightplugins.repair.master;

import de.lightplugins.repair.commands.manager.MainCommandManager;
import de.lightplugins.repair.commands.tabs.MainTabCompleter;
import de.lightplugins.repair.events.DuraWarning;
import de.lightplugins.repair.events.ItemsAdderOnLoad;
import de.lightplugins.repair.inv.CheckItemStack;
import de.lightplugins.repair.inv.CheckOnRepair;
import de.lightplugins.repair.kits.KitBuilder;
import de.lightplugins.repair.util.ColorTranslation;
import de.lightplugins.repair.util.FileManager;
import de.lightplugins.repair.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {


    public static Main getInstance;
    public static final String consolePrefix = "§r[light§cRepair§r] ";
    public static FileManager settings;
    public static FileManager messages;
    public static FileManager kits;
    public static ColorTranslation colorTranslation;
    public static Util util;
    public static KitBuilder kitBuilder;
    public boolean isItemsAdder = false;

    public void onLoad() {


    }


    public void onEnable() {

        getInstance = this;
        settings = new FileManager(this, "settings.yml", true);
        messages = new FileManager(this, "messages.yml", true);
        kits = new FileManager(this, "kits.yml", false);

        util = new Util();
        colorTranslation = new ColorTranslation();

        kitBuilder = new KitBuilder();

        if (Bukkit.getPluginManager().getPlugin("ItemsAdder") == null) {
            Bukkit.getConsoleSender().sendMessage("\n\n    §4ERROR\n\n" +
                    "    §cCould not find §4Itemsadder\n" +
                    "    §rDownload the latest version if ItemsAdder\n" +
                    "    §chttps://builtbybit.com/resources/itemsadder.10839/\n\n\n");
            isItemsAdder = false;
        } else {
            isItemsAdder = true;
        }

        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new CheckOnRepair(), this);
        pm.registerEvents(new CheckItemStack(), this);
        pm.registerEvents(new DuraWarning(), this);

        if(isItemsAdder) {
            pm.registerEvents(new ItemsAdderOnLoad(), this);
        } else {
            kitBuilder.reloadKits();
        }

        Objects.requireNonNull(this.getCommand("lrepair")).setExecutor(new MainCommandManager(this));
        Objects.requireNonNull(this.getCommand("lrepair")).setTabCompleter(new MainTabCompleter());

    }


    public void onDisable() {

    }


}