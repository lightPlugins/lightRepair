package de.lightplugins.repair.kits;

import com.willfp.eco.core.items.Items;
import de.lightplugins.repair.enums.PersistentDataPaths;
import de.lightplugins.repair.master.Main;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.logging.Level;

public class KitBuilder {

    public final Map<String, ItemStack> repairKits = new HashMap<>();
    public final List<String> kitNames = new ArrayList<>();


    public Map<String, ItemStack> getAllKits() {
        return repairKits;
    }

    public void reloadKits() {
        repairKits.clear();
        updateAllKits();
        updateKitNames();
    }

    public void updateItemStack(NamespacedKey key, ItemMeta itemMeta) {

        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        if(key.getKey().equalsIgnoreCase(PersistentDataPaths.DURABILITY_VALUE.getType())) {

            if(!data.has(key, PersistentDataType.INTEGER)) {
                return;
            }

            Integer amount = data.get(key, PersistentDataType.INTEGER);
        }
    }

    public List<String> getKitNames() {
        updateKitNames();
        return kitNames;
    }

    public String getDisplayName(String kitName) {
        FileConfiguration kits = Main.kits.getConfig();
        return Main.colorTranslation.hexTranslation(kits.getString("repairKits." + kitName + ".displayName"));
    }

    public ItemStack getKitByName(String name) {

        for(String key : getAllKits().keySet()) {
            if(key.equalsIgnoreCase(name)) {
                return getAllKits().get(key);
            }
        }

        return new ItemStack(Material.STONE, 1);
    }

    private void updateKitNames() {
        FileConfiguration kits = Main.kits.getConfig();
        kitNames.clear();
        kitNames.addAll(Objects.requireNonNull(kits.getConfigurationSection("repairKits")).getKeys(false));
    }

    private void updateAllKits() {

        FileConfiguration kits = Main.kits.getConfig();

        for(String kitPath : Objects.requireNonNull(kits.getConfigurationSection("repairKits")).getKeys(false)) {

            ItemStack itemStack = new ItemStack(Material.STONE);

            String material = kits.getString("repairKits." + kitPath + ".material");

            if(material == null) {
                return;
            }

            String[] matParams = material.split(":");

            if(matParams.length != 2) {
                return;
            }


            switch (matParams[0]) {
                case "vanilla" -> {
                    itemStack.setType(Material.valueOf(matParams[1]));
                }
                case "itemsadder" -> {
                    itemStack = CustomStack.getInstance(matParams[1]).getItemStack();
                }
                case "ecoitems" -> {
                    itemStack = Items.lookup("ecoitems:" + matParams[1]).getItem();
                }
            }

            String displayName = Main.colorTranslation.hexTranslation(
                    kits.getString("repairKits." + kitPath + ".displayName"));

            ItemMeta itemMeta = itemStack.getItemMeta();

            if(itemMeta == null) {
                return;
            }

            itemMeta.setDisplayName(displayName);
            if(itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            int durability = kits.getInt("repairKits." + kitPath + ".addDurability");

            List<String> lore = new ArrayList<>();
            for(String line : kits.getStringList("repairKits." + kitPath + ".lore")) {
                lore.add(Main.colorTranslation.hexTranslation(line
                        .replace("#durability#", String.valueOf(durability))));
            }
            itemMeta.setLore(lore);

            boolean glow = kits.getBoolean("repairKits." + kitPath + ".glow");
            if(glow) {
                itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            NamespacedKey namespaceKeyValue = new NamespacedKey(
                    Main.getInstance, PersistentDataPaths.DURABILITY_VALUE.getType());
            NamespacedKey namespaceKeyValue1 = new NamespacedKey(
                    Main.getInstance, PersistentDataPaths.KIT_ID.getType());

            if(namespaceKeyValue.getKey().equalsIgnoreCase(PersistentDataPaths.DURABILITY_VALUE.getType())) {
                data.set(namespaceKeyValue, PersistentDataType.INTEGER, durability);
                data.set(namespaceKeyValue1, PersistentDataType.STRING, kitPath);
            }

            itemStack.setItemMeta(itemMeta);
            repairKits.put(kitPath, itemStack);

        }
    }
}
