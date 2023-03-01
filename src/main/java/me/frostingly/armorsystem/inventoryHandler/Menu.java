package me.frostingly.armorsystem.inventoryHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;

    protected PlayerMenuUtility playerMenuUtility;
    protected Player player;

    public Menu(PlayerMenuUtility playerMenuUtility, Player player) {
        this.playerMenuUtility = playerMenuUtility;
        this.player = player;
    }

    public abstract String getInventoryName();

    public abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent e) throws CloneNotSupportedException;

    public abstract void setMenuItems();

    public void open() {

        inventory = Bukkit.createInventory(this, getSlots(), getInventoryName());

        this.setMenuItems();

        playerMenuUtility.getOwner().openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

}
