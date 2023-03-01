package me.frostingly.armorsystem.events;

import me.frostingly.armorsystem.ArmorSystem;
import me.frostingly.armorsystem.Utils;
import me.frostingly.armorsystem.inventories.AllCraftsMenu;
import me.frostingly.armorsystem.inventories.CraftingMenu;
import me.frostingly.armorsystem.inventoryHandler.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryClick implements Listener {

    private final ArmorSystem plugin;

    public InventoryClick(ArmorSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e) throws CloneNotSupportedException {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getHolder() == null) return;
        Player player = (Player) e.getWhoClicked();
        InventoryHolder holder = e.getClickedInventory().getHolder();
        if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
            if (!(Utils.format(e.getView().getTitle()).contains(Utils.format("&bCrafting Menu"))) && !(e.getView().getTitle().equalsIgnoreCase("Crafting"))) {
                e.setCancelled(true);
            }
        }

        if (holder instanceof Menu && !(holder instanceof CraftingMenu)) {
            if (e.getCurrentItem() == null) return;
            Menu menu = (Menu) holder;
            menu.handleMenu(e);
            e.setCancelled(true);
        } else if (holder instanceof CraftingMenu) {
            Menu menu = (Menu) holder;
            List<Integer> badSlots = new ArrayList<>();
            if (menu.getInventory().getItem(26).getType() != Material.GRAY_STAINED_GLASS_PANE) {
                badSlots.add(26);
            }
            menu.handleMenu(e);

            int slot;
            int start = 11;
            for (int rowNumber = 0; rowNumber < 4; rowNumber++) {
                slot = start;
                for (int i = 0; i < 4; i++) {
                    badSlots.add(slot);
                    slot++;
                }
                start += 9;
            }

            if (!badSlots.contains(e.getSlot())) {
                e.setCancelled(true);
            }
        }
    }
}
