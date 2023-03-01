package me.frostingly.armorsystem.inventories;

import me.frostingly.armorsystem.ArmorSystem;
import me.frostingly.armorsystem.Utils;
import me.frostingly.armorsystem.configuration.ItemData;
import me.frostingly.armorsystem.inventoryHandler.Menu;
import me.frostingly.armorsystem.inventoryHandler.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftRecipeMenu extends Menu {

    private final ArmorSystem plugin;
    private final String recipeName;
    private final ItemData itemData;
    private final int page;

    public CraftRecipeMenu(PlayerMenuUtility playerMenuUtility, Player player, ArmorSystem plugin, String recipeName, ItemData itemData, int page) {
        super(playerMenuUtility, player);
        this.plugin = plugin;
        this.recipeName = recipeName;
        this.itemData = itemData;
        this.page = page;
    }

    @Override
    public String getInventoryName() {
        return Utils.format("&bRecipe for " + recipeName);
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws CloneNotSupportedException {
        if (e.getSlot() == 45) {
            new AllCraftsMenu(playerMenuUtility, player, plugin, page).open();
        }
    }

    @Override
    public void setMenuItems() {

        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, createEmptyPane());
        }

        inventory.setItem(45, createBackwardsArrow());

        int slot;
        int start = 11;
        for (int rowNumber : itemData.getCraftingGrid().keySet()) {
            slot = start;
            for (int i = 0; i < itemData.getCraftingGrid().get(rowNumber).size(); i++) {
                inventory.setItem(slot, itemData.getCraftingGrid().get(rowNumber).get(i).getItemStack());
                slot++;
            }
            start += 9;
        }

        ItemStack itemStack = itemData.getItemStack().clone();





        itemStack.setAmount(1); //could be an issue later!





        inventory.setItem(26, itemStack);
    }

    public ItemStack createEmptyPane() {
        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemMeta.setLore(null);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createBackwardsArrow() {
        ItemStack itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Utils.format("&cBack"));
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        return  itemStack;
    }
}
