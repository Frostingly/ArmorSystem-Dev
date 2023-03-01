package me.frostingly.armorsystem.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.frostingly.armorsystem.ArmorSystem;
import me.frostingly.armorsystem.Utils;
import me.frostingly.armorsystem.configuration.Default;
import me.frostingly.armorsystem.configuration.ItemData;
import me.frostingly.armorsystem.inventoryHandler.Menu;
import me.frostingly.armorsystem.inventoryHandler.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

public class CraftingMenu extends Menu implements Listener {
    private final ArmorSystem plugin;

    private Map<Integer, List<ItemData>> itemsInGrid;

    private ItemData foundItemData;

    public CraftingMenu(PlayerMenuUtility playerMenuUtility, Player player, ArmorSystem plugin) {
        super(playerMenuUtility, player);
        this.itemsInGrid = new HashMap<>();
        this.foundItemData = null;
        this.plugin = plugin;
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(this, (Plugin)plugin);
    }

    public String getInventoryName() {
        return Utils.format("&bCrafting Menu");
    }

    public int getSlots() {
        return 54;
    }

    public void handleMenu(final InventoryClickEvent e) throws CloneNotSupportedException {
        Player player = (Player)e.getWhoClicked();
        final Inventory inv = e.getClickedInventory();
        ItemStack current = e.getCurrentItem();
        ItemStack cursor = e.getCursor();
        switch (e.getSlot()) {
            case 45:
                (new AnvilSelectionMenu(this.playerMenuUtility, player, this.plugin)).open();
                break;
            case 26:
                if (e.getCurrentItem().getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    e.setCancelled(false);
                    clearTable(inv, e.getCurrentItem().getAmount());
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                }
                break;
        }
        (new BukkitRunnable() {
            public void run() {
                CraftingMenu.this.isTableValid(inv, e.getSlot());
            }
        }).runTaskLater((Plugin)this.plugin, 1L);
    }

    @EventHandler
    public void spreading(final InventoryDragEvent e) {
        (new BukkitRunnable() {
            public void run() {
                CraftingMenu.this.isTableValid(e.getInventory(), 0);
            }
        }).runTaskLater((Plugin)this.plugin, 1L);
    }

    public void isTableValid(Inventory inventory, int itemSlot) {
        int usedSlots = 0;
        int baseGridAmount = 0;
        int actualGridAmount = 0;
        int start = 11;
        for (int row = 0; row < 4; row++) {
            List<ItemData> itemsInGridList = new ArrayList<>();
            int slot = start;
            for (int i = 0; i < 4; i++) {
                if (slot == itemSlot) {
                    if (inventory.getItem(itemSlot) == null) {
                        ItemData airData = new ItemData("BUILDING_BLOCKS", ((Integer)Default.getItemID().get(Material.AIR)).intValue(), new ItemStack(Material.AIR), slot, null, null, false);
                        itemsInGridList.add(airData);
                    } else {
                        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "id");
                        if (inventory.getItem(slot).getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER) != null) {
                            ItemData slotData = new ItemData("BUILDING_BLOCKS", ((Integer)inventory.getItem(slot).getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER)).intValue(), inventory.getItem(slot), slot, null, null, false);
                            itemsInGridList.add(slotData);
                        } else {
                            ItemData slotData = new ItemData("BUILDING_BLOCKS", ((Integer)Default.getItemID().get(inventory.getItem(slot).getType())).intValue(), inventory.getItem(slot), slot, null, null, false);
                            itemsInGridList.add(slotData);
                        }
                    }
                    slot++;
                } else {
                    if (inventory.getItem(slot) == null) {
                        ItemData airData = new ItemData("BUILDING_BLOCKS", ((Integer)Default.getItemID().get(Material.AIR)).intValue(), new ItemStack(Material.AIR), slot, null, null, false);
                        itemsInGridList.add(airData);
                    } else {
                        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "id");
                        if (inventory.getItem(slot).getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER) != null) {
                            ItemData slotData = new ItemData("BUILDING_BLOCKS", ((Integer)inventory.getItem(slot).getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER)).intValue(), inventory.getItem(slot), slot, null, null, false);
                            itemsInGridList.add(slotData);
                        } else {
                            ItemData slotData = new ItemData("BUILDING_BLOCKS", ((Integer)Default.getItemID().get(inventory.getItem(slot).getType())).intValue(), inventory.getItem(slot), slot, null, null, false);
                            itemsInGridList.add(slotData);
                        }
                    }
                    slot++;
                }
            }
            start += 9;
            this.itemsInGrid.put(Integer.valueOf(row), itemsInGridList);
        }
        boolean found = false;
        Map<Integer, Integer> slotAmount = new HashMap<>();
        for (ItemData itemData : this.plugin.getItemMap().values()) {
            if (itemData.getCraftingGrid() != null && itemData.getCraftingGrid().size() > 0 &&
                    !found) {
                int correctSlots = 0;
                for (int rowNumber = 0; rowNumber < 4; rowNumber++) {
                    for (int i = 0; i < 4; i++) {
                        if (itemData.getCraftingGrid().size() != 3) {
                            int minimumAmountRequired = ((ItemData)((List<ItemData>)itemData.getCraftingGrid().get(Integer.valueOf(rowNumber))).get(i)).getItemStack().getAmount();
                            if (((ItemData)((List<ItemData>)itemData.getCraftingGrid().get(Integer.valueOf(rowNumber))).get(i)).getId() == ((ItemData)((List<ItemData>)this.itemsInGrid
                                    .get(Integer.valueOf(rowNumber))).get(i)).getId() && ((ItemData)((List)this.itemsInGrid.get(Integer.valueOf(rowNumber))).get(i)).getItemStack().getAmount() >= minimumAmountRequired)
                                correctSlots++;
                        }
                    }
                }
                if (correctSlots == 16) {
                    found = true;
                    this.foundItemData = itemData;
                    start = 11;
                    int i;
                    for (i = 0; i < 4; i++) {
                        int slot = start;
                        for (int j = 0; j < 4; j++) {
                            if (((ItemData)((List<ItemData>)this.foundItemData.getCraftingGrid().get(Integer.valueOf(i))).get(j)).getItemStack().getType() != Material.AIR)
                                usedSlots++;
                            if (((ItemData)((List<ItemData>)this.itemsInGrid.get(Integer.valueOf(i))).get(j)).getSlot() == slot && ((ItemData)((List<ItemData>)this.foundItemData

                                    .getCraftingGrid().get(Integer.valueOf(i))).get(j)).getSlot() == slot)
                                if (((ItemData)((List)this.itemsInGrid.get(Integer.valueOf(i))).get(j)).getItemStack().getAmount() >= ((ItemData)((List)this.foundItemData
                                        .getCraftingGrid().get(Integer.valueOf(i))).get(j)).getItemStack().getAmount() * 2)
                                    slotAmount.put(Integer.valueOf(slot), Integer.valueOf(((ItemData)((List<ItemData>)this.itemsInGrid.get(Integer.valueOf(i))).get(j)).getItemStack().getAmount()));
                            slot++;
                        }
                        start += 9;
                    }
                    if (slotAmount.size() == usedSlots) {
                        start = 11;
                        for (i = 0; i < 4; i++) {
                            int slot = start;
                            for (int k = 0; k < 4; k++) {
                                if (((ItemData)((List<ItemData>)this.foundItemData.getCraftingGrid().get(Integer.valueOf(i))).get(k)).getItemStack().getType() != Material.AIR && ((ItemData)((List<ItemData>)this.itemsInGrid
                                        .get(Integer.valueOf(i))).get(k)).getItemStack().getType() != Material.AIR) {
                                    baseGridAmount += ((ItemData)((List<ItemData>)this.foundItemData.getCraftingGrid().get(Integer.valueOf(i))).get(k)).getItemStack().getAmount();
                                    actualGridAmount += ((ItemData)((List<ItemData>)this.itemsInGrid.get(Integer.valueOf(i))).get(k)).getItemStack().getAmount();
                                }
                                slot++;
                            }
                            start += 9;
                        }
                        int highestAmount = 2;
                        int highestIndex = 2;
                        for (int j = 0; j < 4; j++) {
                            for (int k = 0; k < 4; k++) {
                                if (((ItemData)((List<ItemData>)this.itemsInGrid.get(Integer.valueOf(j))).get(k)).getItemStack().getAmount() != 1 && (
                                        (ItemData)((List)this.itemsInGrid.get(Integer.valueOf(j))).get(k)).getItemStack().getAmount() > highestIndex)
                                    highestIndex = ((ItemData)((List<ItemData>)this.itemsInGrid.get(Integer.valueOf(j))).get(k)).getItemStack().getAmount();
                            }
                        }
                        int correctMultipleSlots = 0;
                        for (int highestAmountTwo = highestAmount; highestAmountTwo <= highestIndex; highestAmountTwo++) {
                            for (int k = 0; k < 4; k++) {
                                for (int m = 0; m < 4; m++) {
                                    if (((ItemData)((List<ItemData>)this.itemsInGrid.get(Integer.valueOf(k))).get(m)).getItemStack().getType() != Material.AIR && ((ItemData)((List<ItemData>)this.foundItemData
                                            .getCraftingGrid().get(Integer.valueOf(k))).get(m)).getItemStack().getType() != Material.AIR && (
                                            (ItemData)((List)this.itemsInGrid.get(Integer.valueOf(k))).get(m)).getItemStack().getAmount() / ((ItemData)((List)this.foundItemData
                                            .getCraftingGrid().get(Integer.valueOf(k))).get(m)).getItemStack().getAmount() >= highestAmountTwo)
                                        correctMultipleSlots++;
                                }
                            }
                            if (correctMultipleSlots == usedSlots) {
                                correctMultipleSlots = 0;
                                highestAmount = highestAmountTwo;
                                ItemStack clone = this.foundItemData.getItemStack().clone();
                                clone.setAmount(clone.getAmount() * highestAmount);
                                inventory.setItem(26, clone);
                            } else {
                                correctMultipleSlots = 0;
                            }
                        }
                        continue;
                    }
                    inventory.setItem(26, this.foundItemData.getItemStack());
                    continue;
                }
                inventory.setItem(26, createEmptyPane());
                this.foundItemData = null;
            }
        }
    }

    public void setMenuItems() {
        int i;
        for (i = 0; i < 54; i++)
            this.inventory.setItem(i, createEmptyPane());
        for (i = 0; i < 54; i++) {
            this.inventory.setItem(i, createUnknownRecipe());
            i += 8;
        }
        this.inventory.setItem(45, createBackwardsArrow());
        int start = 11;
        for (int rowNumber = 0; rowNumber < 4; rowNumber++) {
            int slot = start;
            for (int j = 0; j < 4; j++) {
                ItemStack itemStack = new ItemStack(Material.AIR);
                this.inventory.setItem(slot, itemStack);
                slot++;
            }
            start += 9;
        }
    }

    public void clearTable(Inventory inventory, int result) {
        int start = 11;
        for (int rowNumber = 0; rowNumber < 4; rowNumber++) {
            int slot = start;
            for (int i = 0; i < 4; i++) {
                if (inventory.getItem(slot) != null)
                    inventory.getItem(slot).setAmount(inventory.getItem(slot).getAmount() - ((ItemData)((List<ItemData>)this.foundItemData.getCraftingGrid().get(Integer.valueOf(rowNumber))).get(i)).getItemStack().getAmount() * result);
                slot++;
            }
            start += 9;
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

    public ItemStack createUnknownRecipe() {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Utils.format("&cNo possible item yet."));
        itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createBackwardsArrow() {
        ItemStack itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Utils.format("&cBack"));
        itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}