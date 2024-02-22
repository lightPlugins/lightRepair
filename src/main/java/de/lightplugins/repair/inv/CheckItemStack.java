package de.lightplugins.repair.inv;

import de.lightplugins.repair.enums.PersistentDataPaths;
import de.lightplugins.repair.master.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
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

        if(event.getClickedInventory() == null) {
            return;
        }

        if(event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null) {
            return;
        }

        int itemAmount = itemStack.getAmount();

        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta == null) {
            return;
        }

        for(String kitName : Main.kitBuilder.getAllKits().keySet()) {

            ItemStack is = Main.kitBuilder.getKitByName(kitName).clone();

            is.setAmount(itemAmount);
            if(is.equals(itemStack)) {
                continue;
            }

            PersistentDataContainer invData = itemMeta.getPersistentDataContainer();
            NamespacedKey invKey = new NamespacedKey(Main.getInstance, PersistentDataPaths.DURABILITY_VALUE.getType());

            ItemMeta itemMetaList = is.getItemMeta();

            if(itemMetaList == null) {
                continue;
            }

            PersistentDataContainer listData = itemMetaList.getPersistentDataContainer();
            NamespacedKey listKey = new NamespacedKey(Main.getInstance, PersistentDataPaths.DURABILITY_VALUE.getType());
            if(!invKey.getKey().equalsIgnoreCase(PersistentDataPaths.DURABILITY_VALUE.getType())) {
                continue;
            }

            if(!invData.has(invKey, PersistentDataType.INTEGER)) {
                continue;
            }

            Integer invAmount = invData.get(invKey, PersistentDataType.INTEGER);
            Integer listAmount = listData.get(listKey, PersistentDataType.INTEGER);

            if(Objects.equals(invAmount, listAmount)) {
                event.setCancelled(true);
                itemStack = is;
                //update players current inventory
                event.getClickedInventory().setItem(event.getSlot(), itemStack);

            }
        }
    }
}
