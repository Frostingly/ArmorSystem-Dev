package me.frostingly.armorsystem;

import me.frostingly.armorsystem.configuration.Default;
import me.frostingly.armorsystem.configuration.ItemData;
import me.frostingly.armorsystem.configuration.Register.RegisterItems;
import me.frostingly.armorsystem.events.AnvilClick;
import me.frostingly.armorsystem.events.InventoryClick;
import me.frostingly.armorsystem.inventoryHandler.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class ArmorSystem extends JavaPlugin {

    private Map<Integer, ItemData> itemMap = new HashMap<>();
    private Map<UUID, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        new Default().loadValues();
        new RegisterItems(this).registerItems();
        new RegisterItems(this).registerCraftingGrids();
        this.getLogger().info("Registered " + itemMap.size() + " items.");
        loadEvents();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new AnvilClick(this), this);
        pm.registerEvents(new InventoryClick(this), this);
    }

    public Map<UUID, PlayerMenuUtility> getPlayerMenuUtilityMap() {
        return playerMenuUtilityMap;
    }

    public Map<Integer, ItemData> getItemMap() {
        return itemMap;
    }
}
