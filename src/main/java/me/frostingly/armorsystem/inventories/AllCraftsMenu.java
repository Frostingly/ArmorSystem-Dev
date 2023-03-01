package me.frostingly.armorsystem.inventories;

import me.frostingly.armorsystem.ArmorSystem;
import me.frostingly.armorsystem.Utils;
import me.frostingly.armorsystem.configuration.ItemData;
import me.frostingly.armorsystem.inventoryHandler.Menu;
import me.frostingly.armorsystem.inventoryHandler.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class AllCraftsMenu extends Menu {

    private final ArmorSystem plugin;
    private final int page;

    private int custom = 2; // dont change this (0 - 3)

    public Integer getMaxPages() {
        int amountOfItems = plugin.getItemMap().size();
        int pages = 1;
        if (amountOfItems > 18 + (9 * custom)) {
            pages = (amountOfItems / (18 + (9 * custom))) + 1;
        }
        return pages;
    }

    public AllCraftsMenu(PlayerMenuUtility playerMenuUtility, Player player, ArmorSystem plugin, int page) {
        super(playerMenuUtility, player);
        this.plugin = plugin;
        this.page = page;
    }

    @Override
    public String getInventoryName() {
        return Utils.format("&bAll Crafts (Page " + page + "/" + getMaxPages() + ")");
    }

    @Override
    public int getSlots() {
        return 27 + (9 * custom);
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws CloneNotSupportedException {
        int slot1 = (26 + (9 * custom));
        int slot2 = (18 + (9 * custom));
        if (e.getSlot() == slot1 && e.getCurrentItem().equals(createForwardsArrow())) {
            new AllCraftsMenu(playerMenuUtility, player, plugin, page + 1).open();
        } else if (e.getSlot() == slot2 && e.getCurrentItem().equals(createBackwardsArrow(true))) {
            if (page > 1) {
                new AllCraftsMenu(playerMenuUtility, player, plugin, page - 1).open();
            }
        } else if (e.getSlot() == slot2 && e.getCurrentItem().equals(createBackwardsArrow(false))) {
            if (page == 1) {
                new AnvilSelectionMenu(playerMenuUtility, player, plugin).open();
            }
        } else if (e.getSlot() != slot1 && e.getSlot() != slot2 && e.getCurrentItem().getType() != Material.AIR) {
            NamespacedKey key = new NamespacedKey(plugin, "item-id");
            ItemMeta itemMeta = e.getCurrentItem().getItemMeta();
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            if (plugin.getItemMap().get(container.get(key, PersistentDataType.INTEGER)) == null) return;
            if (plugin.getItemMap().get(container.get(key, PersistentDataType.INTEGER)).isCraftable()) {
                new CraftRecipeMenu(playerMenuUtility, player, plugin, e.getCurrentItem().getItemMeta().getDisplayName(), plugin.getItemMap().get(container.get(key, PersistentDataType.INTEGER)), page).open();
            }
        }
    }

    @Override
    public void setMenuItems() {

        for (int i = (18 + (9 * (custom))); i < (27 + (9 * (custom))); i++) {
            inventory.setItem(i, createEmptyPane());
        }

        int slot = -1;
        int iterations = 1;
        int elements = 0;
        if (page > 1) {
            elements = (27 + (9 * (custom - 1))) * (page - 1);
        }
        for (Integer id : plugin.getItemMap().keySet().stream().skip(elements).toList()) {
            ItemData itemData = plugin.getItemMap().get(id);
            ItemStack itemStack = itemData.getItemStack().clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            NamespacedKey key = new NamespacedKey(plugin, "item-id");
            itemMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, id);




            itemStack.setAmount(1); //could be an issue later!!!




            itemStack.setItemMeta(itemMeta);
            if (slot + iterations > (26 + (9 * (custom - 1)))) {
                inventory.setItem((26 + (9 * (custom))), createForwardsArrow());
            } else {
                inventory.setItem((slot + iterations), itemStack);
                iterations++;
            }
        }
        if (page > 1) {
           inventory.setItem((18 + (9 * (custom))), createBackwardsArrow(true));
        }
        if (page == 1) {
            inventory.setItem((18 + (9 * (custom))), createBackwardsArrow(false));
        }
    }
    public ItemStack createEmptyPane() {
        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemMeta.setLore(null);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public ItemStack createBackwardsArrow(boolean backorbackwards) {
        ItemStack itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (backorbackwards) {
            itemMeta.setDisplayName(Utils.format("&cPage " + (page - 1)));
        } else {
            itemMeta.setDisplayName(Utils.format("&cBack"));
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        return  itemStack;
    }

    public ItemStack createForwardsArrow() {
        ItemStack itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Utils.format("&aPage " + (page + 1)));
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        return  itemStack;
    }
}
