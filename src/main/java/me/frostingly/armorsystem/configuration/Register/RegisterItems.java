package me.frostingly.armorsystem.configuration.Register;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.frostingly.armorsystem.ArmorSystem;
import me.frostingly.armorsystem.Utils;
import me.frostingly.armorsystem.configuration.ArmorPiece;
import me.frostingly.armorsystem.configuration.BuildingMaterial;
import me.frostingly.armorsystem.configuration.Default;
import me.frostingly.armorsystem.configuration.ItemData;
import me.frostingly.armorsystem.configuration.Role;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class RegisterItems {
    private final ArmorSystem plugin;

    boolean testing;

    public RegisterItems(ArmorSystem plugin) {
        this.testing = false;
        this.plugin = plugin;
    }

    public void registerItems() {
        File root = new File(this.plugin.getDataFolder(), "items");
        if (root.listFiles() != null) {
            int itemID = 0;
            for (File itemFile : root.listFiles()) {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(itemFile);
                boolean isValid = ((List)Stream.<Role>of(Role.values()).map(Enum::name).collect(Collectors.toList())).contains(yamlConfiguration.get("item.role"));
                if (isValid) {
                    Object value = null;
                    if (yamlConfiguration.getString("item.role").equalsIgnoreCase("ARMOR_PIECE"))
                        value = new ArmorPiece(yamlConfiguration.getInt("item.base_stats.health"), yamlConfiguration.getInt("item.base_stats.defense"), yamlConfiguration.getInt("item.base_stats.stamina"));
                    if (yamlConfiguration.getString("item.role").equalsIgnoreCase("BUILDING_MATERIAL"))
                        value = new BuildingMaterial();
                    ItemStack itemStack = new ItemStack(Material.valueOf(yamlConfiguration.getString("item.exact_item")));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(Utils.format(yamlConfiguration.getString("item.name")));
                    itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
                    NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "id");
                    itemMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, Integer.valueOf(yamlConfiguration.getInt("item.id")));
                    itemStack.setItemMeta(itemMeta);
                    ItemData itemData = null;
                    if (yamlConfiguration.getConfigurationSection("item.crafting_grid") != null)
                        itemData = new ItemData(value, yamlConfiguration.getInt("item.id"), itemStack, 0, (Configuration)yamlConfiguration, null, true);
                    if (yamlConfiguration.getConfigurationSection("item.crafting_grid") == null)
                        itemData = new ItemData(value, yamlConfiguration.getInt("item.id"), itemStack, 0, (Configuration)yamlConfiguration, null, false);
                    if (!this.testing) {
                        this.plugin.getItemMap().put(Integer.valueOf(yamlConfiguration.getInt("item.id")), itemData);
                    } else {
                        this.plugin.getItemMap().put(Integer.valueOf(itemID), itemData);
                        itemID++;
                    }
                } else {
                    Bukkit.getLogger().severe(yamlConfiguration.getString("item.role") + " doesn't exist or isn't valid. ERROR CODE: Q44");
                }
            }
        } else {
            Bukkit.getLogger().severe("No files have been found. ERROR CODE: Q33");
        }
    }

    public void registerCraftingGrids() {
        for (ItemData itemData : this.plugin.getItemMap().values()) {
            Configuration config = itemData.getConfig();
            Map<Integer, List<ItemData>> craftingGrid = new HashMap<>();
            int rowNumber = 0;
            int start = 11;
            if (config.getConfigurationSection("item.crafting_grid") != null)
                for (String section : config.getConfigurationSection("item.crafting_grid").getKeys(false)) {
                    int slot = start;
                    ConfigurationSection rowConfig = config.getConfigurationSection("item.crafting_grid." + section);
                    String row = rowConfig.getString(".row");
                    List<ItemData> currrentRowItemdatas = new ArrayList<>();
                    ItemData craftingGridItemData = null;
                    for (int i = 0; i < (row.split(", ")).length; i++) {
                        String splitRow = row.split(", ")[i];
                        int amount = 1;
                        if (splitRow.contains("(")) {
                            if (!splitRow.contains("custom(")) {
                                if (Integer.parseInt(splitRow.split("\\(")[1].split("\\)")[0]) > 0) {
                                    amount = Integer.parseInt(splitRow.split("\\(")[1].split("\\)")[0]);
                                } else if (Integer.parseInt(splitRow.split("\\(")[1].split("\\)")[0]) <= 0) {
                                    this.plugin.getLogger().severe("Attempted to have amount less than or equal to 0. Automatically defaulted the amount to 1 for " +

                                            Utils.format(config.getString("item.name")));
                                }
                                ItemStack itemStack = new ItemStack(Material.valueOf(row.split(", ")[i].split("\\(")[0]), amount);
                                craftingGridItemData = new ItemData("BUILDING_MATERIAL", ((Integer)Default.getItemID().get(itemStack.getType())).intValue(), itemStack, slot, null, null, false);
                            } else {
                                int id = Integer.parseInt(splitRow.split("\\(")[1].split("\\)")[0]);
                                if (splitRow.split("\\(")[2].split("\\)")[0] != null)
                                    if (Integer.parseInt(splitRow.split("\\(")[2].split("\\)")[0]) > 0) {
                                        amount = Integer.parseInt(splitRow.split("\\(")[2].split("\\)")[0]);
                                    } else {
                                        this.plugin.getLogger().severe("Attempted to have amount less than or equal to 0. Automatically defaulted the amount to 1 for " +

                                                Utils.format(config.getString("item.name")));
                                    }
                                ItemStack itemStack = ((ItemData)this.plugin.getItemMap().get(Integer.valueOf(id))).getItemStack();
                                craftingGridItemData = new ItemData("BUILDING_MATERIAL", id, itemStack, slot, null, null, false);
                            }
                        } else {
                            ItemStack itemStack = new ItemStack(Material.valueOf(row.split(", ")[i]), amount);
                            craftingGridItemData = new ItemData("BUILDING_MATERIAL", ((Integer)Default.getItemID().get(itemStack.getType())).intValue(), itemStack, slot, null, null, false);
                        }
                        currrentRowItemdatas.add(craftingGridItemData);
                        slot++;
                    }
                    craftingGrid.put(Integer.valueOf(rowNumber), currrentRowItemdatas);
                    rowNumber++;
                    start += 9;
                }
            itemData.setCraftingGrid(craftingGrid);
        }
    }
}
