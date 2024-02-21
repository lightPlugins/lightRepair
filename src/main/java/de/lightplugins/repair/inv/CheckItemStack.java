package de.lightplugins.repair.inv;

import de.lightplugins.repair.enums.PersistentDataPaths;
import de.lightplugins.repair.master.Main;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class CheckItemStack implements Listener {

    @EventHandler
    public void onItemUpdate(InventoryClickEvent event) {

        for (ItemStack itemStack : event.getInventory().getContents()) {

            int itemAmount = itemStack.getAmount();

            ItemMeta itemMeta = itemStack.getItemMeta();
            if(itemMeta == null) {
                return;
            }

            for(String kitName : Main.kitBuilder.getAllKits().keySet()) {

                ItemStack is = Main.kitBuilder.getAllKits().get(kitName);

                is.setAmount(itemAmount);
                if(is.equals(itemStack)) {
                    Bukkit.getLogger().log(Level.INFO, "invStack and listStack are equal");
                    return;
                }

                PersistentDataContainer invData = itemMeta.getPersistentDataContainer();
                NamespacedKey invKey = new NamespacedKey(Main.getInstance, PersistentDataPaths.DURABILITY_VALUE.getType());

                ItemMeta itemMetaList = is.getItemMeta();

                if(itemMetaList == null) {
                    Bukkit.getLogger().log(Level.INFO, "itemMetaList is null");
                    return;
                }

                PersistentDataContainer listData = itemMetaList.getPersistentDataContainer();
                NamespacedKey listKey = new NamespacedKey(Main.getInstance, PersistentDataPaths.DURABILITY_VALUE.getType());
                if(!invKey.getKey().equalsIgnoreCase(PersistentDataPaths.DURABILITY_VALUE.getType())) {
                    Bukkit.getLogger().log(Level.INFO, "invKey has no durability value");
                    return;
                }

                if(!invData.has(invKey, PersistentDataType.INTEGER)) {
                    Bukkit.getLogger().log(Level.INFO, "invData has no durability key");
                    return;
                }

                Integer invAmount = invData.get(invKey, PersistentDataType.INTEGER);
                Integer listAmount = listData.get(listKey, PersistentDataType.INTEGER);

                if(Objects.equals(invAmount, listAmount)) {
                    Bukkit.getLogger().log(Level.INFO, "durability value is equal. Update current item ...");
                    itemStack = is;
                }
            }
        }
    }
}
