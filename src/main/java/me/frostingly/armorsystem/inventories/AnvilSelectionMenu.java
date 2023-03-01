package me.frostingly.armorsystem.inventories;

import me.frostingly.armorsystem.ArmorSystem;
import me.frostingly.armorsystem.Utils;
import me.frostingly.armorsystem.inventoryHandler.Menu;
import me.frostingly.armorsystem.inventoryHandler.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AnvilSelectionMenu extends Menu {

    private ArmorSystem plugin;

    public AnvilSelectionMenu(PlayerMenuUtility playerMenuUtility, Player player, ArmorSystem plugin) {
        super(playerMenuUtility, player);
        this.plugin = plugin;
    }

    @Override
    public String getInventoryName() {
        return Utils.format("&bPlease select!");
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws CloneNotSupportedException {
        inventory.setItem(e.getSlot(), new ItemStack(Material.AIR));
        switch (e.getSlot()) {
            case 2:
                new AllCraftsMenu(playerMenuUtility, player, plugin, 1).open();
                break;
            case 4:
                new CraftingMenu(playerMenuUtility, player, plugin).open();
                break;
        }
    }
    
    @Override
    public void setMenuItems() {
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, createEmptyPane());
        }

        inventory.setItem(2, createMenu());
        inventory.setItem(4, createCraftingTable());
        inventory.setItem(6, createAnvil());
    }

    public ItemStack createEmptyPane() {
        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemMeta.setLore(null);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createMenu() {
        ItemStack itemStack = new ItemStack(Material.COMPASS);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Utils.format("&fCompass"));
        List<String> lore = new ArrayList<>();
        lore.add("&7Click to view all crafts!");
        lore = Utils.format(lore);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createCraftingTable() {
        ItemStack itemStack = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Utils.format("&fCrafting menu"));
        List<String> lore = new ArrayList<>();
        lore.add("&7Click to open the crafting menu!");
        lore.add("&7Here you can craft items!");
        lore = Utils.format(lore);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createAnvil() {
        ItemStack itemStack = new ItemStack(Material.ANVIL);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Utils.format("&fAnvil menu"));
        List<String> lore = new ArrayList<>();
        lore.add("&7Click to open the anvil menu!");
        lore.add("&7Here you can combine items with buff items!");
        lore = Utils.format(lore);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
