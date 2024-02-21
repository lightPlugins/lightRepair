package de.lightplugins.repair.kits;

import com.willfp.eco.core.items.Items;
import de.lightplugins.repair.enums.PersistentDataPaths;
import de.lightplugins.repair.master.Main;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KitBuilder {

    public final List<ItemStack> repairKits = new ArrayList<>();


    public List<ItemStack> getAllKits() {
        return repairKits;
    }

    public void reloadKits() {
        repairKits.clear();
        createSingleKit();
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

    private void createSingleKit() {

        FileConfiguration kits = Main.kits.getConfig();

        for(String kitPath : Objects.requireNonNull(kits.getConfigurationSection("repairKits")).getKeys(false)) {

            ItemStack itemStack = new ItemStack(Material.STONE, 1);
            ItemMeta itemMeta = itemStack.getItemMeta();

            if(itemMeta == null) {
                return;
            }

            String material = kits.getString("repairKits." + kitPath + "material");

            if(material == null) {
                return;
            }

            String[] matParams = material.split(":");

            if(matParams.length != 2) {
                return;
            }

            switch (matParams[0]) {

                case "vanilla":
                    itemStack.setType(Material.valueOf(matParams[1]));
                    break;

                case "itemsadder":
                    itemStack = CustomStack.getInstance(matParams[1]).getItemStack();
                    break;

                case "ecoitems":
                    itemStack = Items.lookup("ecoitems:" + matParams[1]).getItem();
                    break;
            }

            String displayName = Main.colorTranslation.hexTranslation(
                    kits.getString("repairKits." + kitPath + "displayName"));

            itemMeta.setDisplayName(displayName);
            if(itemMeta.getLore() != null) {
                itemMeta.getLore().clear();
            }

            int durability = kits.getInt("repairKits." + kitPath + "addDurability");

            List<String> lore = new ArrayList<>();
            for(String line : kits.getStringList("repairKits." + kitPath + "lore")) {
                lore.add(Main.colorTranslation.hexTranslation(line
                        .replace("#durability#", String.valueOf(durability))));
            }
            itemMeta.setLore(lore);

            boolean glow = kits.getBoolean("repairKits." + kitPath + "glow");
            if(glow) {
                itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            NamespacedKey namespaceKeyValue = new NamespacedKey(
                    Main.getInstance, PersistentDataPaths.DURABILITY_VALUE.getType());

            if(namespaceKeyValue.getKey().equalsIgnoreCase(PersistentDataPaths.DURABILITY_VALUE.getType())) {
                data.set(namespaceKeyValue, PersistentDataType.INTEGER, durability);
            }

            itemStack.setItemMeta(itemMeta);
            repairKits.add(itemStack);

        }
    }
}
