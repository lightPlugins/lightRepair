package de.lightplugins.repair.events;

import de.lightplugins.repair.enums.MessagePath;
import de.lightplugins.repair.master.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.logging.Level;

public class DuraWarning implements Listener {


    @EventHandler
    public void onDuraWarning(PlayerItemDamageEvent event) {

        Player player = event.getPlayer();
        FileConfiguration settings = Main.settings.getConfig();

        if(!settings.getBoolean("settings.durabilityWarning.enable")) {
            return;
        }

        String[] title = Objects.requireNonNull(Main.colorTranslation.hexTranslation(
                settings.getString("settings.durabilityWarning.title"))).split(";");
        String[] sound = Objects.requireNonNull(
                settings.getString("settings.durabilityWarning.sound")).split(";");

        int startUnder = settings.getInt("settings.durabilityWarning.startUnder");


        ItemStack itemStack = event.getItem();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if(itemMeta == null) {
            return;
        }

        if(itemMeta instanceof Damageable damageable) {

            int maxDura = itemStack.getType().getMaxDurability();
            // 0 = fully repaired
            int currentDura = damageable.getDamage();

            int remainingDura = maxDura - currentDura;

            if(remainingDura < startUnder && remainingDura > 0) {

                player.sendTitle(
                        title[0],
                        title[1].replace("#amount#", String.valueOf(remainingDura)),
                        10, 45, 25);
                player.playSound(
                        player.getLocation(),
                        Sound.valueOf(sound[0].toUpperCase()),
                        Float.parseFloat(sound[1]),
                        Float.parseFloat(sound[2]));

            }
        }
    }
}
