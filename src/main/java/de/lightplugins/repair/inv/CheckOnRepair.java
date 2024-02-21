package de.lightplugins.repair.inv;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class CheckOnRepair implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getClickedInventory().getType() == InventoryType.PLAYER) {
            if (event.getCursor() != null && event.getCurrentItem() != null) {
                if (event.getCursor().getType() == Material.DIAMOND) {

                    Player player = (Player) event.getWhoClicked();
                    // Der Spieler zieht einen Diamanten auf ein Diamantschwert
                    ItemStack sword = event.getCurrentItem().clone(); // Kopiere das Diamantschwert


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
                                        material.toString().equals("SHIELD");
                            })
                            .toList();

                    for(ItemStack singleItemFromList : filteredItems) {

                        ItemMeta swordMeta = sword.getItemMeta();

                        if(sword.getType().equals(singleItemFromList.getType())) {

                            if(swordMeta == null) {
                                return;
                            }

                            if(swordMeta instanceof Damageable test) {

                                // Erhöhe die Haltbarkeit des Diamantschwertes um die Haltbarkeit des Diamanten
                                if(sword.getDurability() == 0) {
                                    player.sendMessage("§cDas Item ist bereits komplett repariert!");
                                    event.setCancelled(true);
                                    return;
                                }
                                int newDurability = sword.getDurability() - 250;
                                if (newDurability >= 0) {
                                    sword.setDurability((short) newDurability);
                                    // Entferne den Diamanten vom Inventar des Spielers
                                    player.sendMessage("Durability wurde um 250 erhöht.");

                                    if (event.getCursor().getAmount() == 1) {
                                        player.setItemOnCursor(null);
                                    } else {
                                        event.getCursor().setAmount(event.getCursor().getAmount() - 1);
                                    }
                                    event.setCurrentItem(sword); // Setze das modifizierte Diamantschwert zurück in den Slot
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
