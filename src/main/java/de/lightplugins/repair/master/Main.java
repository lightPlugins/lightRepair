package de.lightplugins.repair.master;

import de.lightplugins.repair.inv.CheckItemStack;
import de.lightplugins.repair.inv.CheckOnRepair;
import de.lightplugins.repair.kits.KitBuilder;
import de.lightplugins.repair.util.ColorTranslation;
import de.lightplugins.repair.util.FileManager;
import de.lightplugins.repair.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {


    public static Main getInstance;
    public static final String consolePrefix = "§r[light§cRepair§r] ";
    public static FileManager settings;
    public static FileManager messages;
    public static FileManager kits;
    public static ColorTranslation colorTranslation;
    public static Util util;
    public static KitBuilder kitBuilder;
    public void onLoad() {


    }


    public void onEnable() {

        getInstance = this;
        settings = new FileManager(this, "settings.yml");
        messages = new FileManager(this, "messages.yml");
        kits = new FileManager(this, "kits.yml");

        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new CheckOnRepair(), this);
        pm.registerEvents(new CheckItemStack(), this);

        util = new Util();
        colorTranslation = new ColorTranslation();

        kitBuilder = new KitBuilder();

    }


    public void onDisable() {

    }


}