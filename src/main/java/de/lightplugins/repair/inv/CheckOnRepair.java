package de.lightplugins.repair.inv;

import de.lightplugins.repair.enums.MessagePath;
import de.lightplugins.repair.enums.PersistentDataPaths;
import de.lightplugins.repair.master.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class CheckOnRepair implements Listener {

    /**
     * This function handles the InventoryClickEvent and performs various operations based on the event's properties and the player's actions.
     *
     * @param  event  The InventoryClickEvent triggered by the player's interaction with an inventory.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getClickedInventory().getType() == InventoryType.PLAYER) {
            if (event.getCursor() != null && event.getCurrentItem() != null) {

                if(event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
                    return;
                }

                ItemStack sword = event.getCurrentItem().clone();
                for(String kitNames : Main.kitBuilder.kitNames) {

                    ItemStack is = Main.kitBuilder.getKitByName(kitNames).clone();

                    is.setAmount(event.getCursor().getAmount());

                    if (event.getCursor().equals(is)) {
                        ItemMeta ism = is.getItemMeta();

                        if(ism == null) {
                            return;
                        }

                        PersistentDataContainer data = ism.getPersistentDataContainer();
                        NamespacedKey key = new NamespacedKey(Main.getInstance, PersistentDataPaths.DURABILITY_VALUE.getType());

                        if(key.getKey().equalsIgnoreCase(PersistentDataPaths.DURABILITY_VALUE.getType())) {

                            if(!data.has(key, PersistentDataType.INTEGER)) {
                                return;
                            }

                            Integer durabilityValue = data.get(key, PersistentDataType.INTEGER);

                            int durability = 0;

                            if (durabilityValue != null) {
                                durability = durabilityValue;
                            }

                            Player player = (Player) event.getWhoClicked();

                            List<ItemStack> items = Arrays.stream(Material.values())
                                    .map(ItemStack::new)
                                    .toList();
                            List<ItemStack> filteredItems = items.stream()
                                    .filter(item -> {
                                        Material material = item.getType();
                                        return material.toString().endsWith("_SWORD") ||
                                                material.toString().endsWith("_AXE") ||
                                                material.toString().endsWith("_SHOVEL") ||
                                                material.toString().endsWith("_HOE") ||
                                                material.toString().endsWith("_PICKAXE") ||
                                                material.toString().endsWith("_HELMET") ||
                                                material.toString().endsWith("_CHESTPLATE") ||
                                                material.toString().endsWith("_LEGGINGS") ||
                                                material.toString().endsWith("_BOOTS") ||
                                                material.toString().equals("ELYTRA") ||
                                                material.toString().equals("FISHING_ROD") ||
                                                material.toString().equals("SHIELD");
                                    })
                                    .toList();

                            for(ItemStack singleItemFromList : filteredItems) {
                                ItemMeta swordMeta = sword.getItemMeta();

                                if(sword.getType().equals(singleItemFromList.getType())) {
                                    if(swordMeta == null) {
                                        return;
                                    }

                                    if(swordMeta instanceof Damageable damageable) {

                                        if(sword.getDurability() == 0) {
                                            Main.util.sendMessage(player, MessagePath.AlreadyFullRepaired.getPath());
                                            Main.util.playSoundOnFail(player);
                                            event.setCancelled(true);
                                            return;
                                        }
                                        int newDurability = sword.getDurability() - durability;
                                        if (newDurability >= 0) {
                                            sword.setDurability((short) newDurability);

                                        } else {
                                            sword.setDurability((short) 0);
                                        }

                                        Main.util.sendMessage(player, MessagePath.OnSuccessRepair.getPath()
                                                .replace("#kit#", Main.kitBuilder.getDisplayName(kitNames)));
                                        Main.util.playSoundOnRepair(player);

                                        if (event.getCursor().getAmount() == 1) {
                                            player.setItemOnCursor(null);
                                        } else {
                                            event.getCursor().setAmount(event.getCursor().getAmount() - 1);
                                        }
                                        event.setCurrentItem(sword);
                                        event.setCancelled(true);
                                        return;

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
