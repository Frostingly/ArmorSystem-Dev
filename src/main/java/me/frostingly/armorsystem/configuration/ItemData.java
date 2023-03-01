package me.frostingly.armorsystem.configuration;

import java.util.List;
import java.util.Map;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;

public class ItemData {
    private final Object value;

    private final int id;

    private final ItemStack itemStack;

    private final int slot;

    private final Configuration config;

    private Map<Integer, List<ItemData>> craftingGrid;

    private final boolean craftable;

    public ItemData(Object value, int id, ItemStack itemStack, int slot, Configuration config, Map<Integer, List<ItemData>> craftingGrid, boolean craftable) {
        this.value = value;
        this.id = id;
        this.itemStack = itemStack;
        this.slot = slot;
        this.config = config;
        this.craftingGrid = craftingGrid;
        this.craftable = craftable;
    }

    public Object getValue() {
        return this.value;
    }

    public int getId() {
        return this.id;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public int getSlot() {
        return this.slot;
    }

    public Configuration getConfig() {
        return this.config;
    }

    public Map<Integer, List<ItemData>> getCraftingGrid() {
        return this.craftingGrid;
    }

    public void setCraftingGrid(Map<Integer, List<ItemData>> craftingGrid) {
        this.craftingGrid = craftingGrid;
    }

    public boolean isCraftable() {
        return this.craftable;
    }
}
