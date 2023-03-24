package me.frostingly.armorsystem.events;

import me.frostingly.armorsystem.ArmorSystem;
import me.frostingly.armorsystem.inventories.AnvilSelectionMenu;
import me.frostingly.armorsystem.inventoryHandler.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class AnvilClick implements Listener {

    private final ArmorSystem plugin;

    public AnvilClick(ArmorSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAnvilClickEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getClickedBlock() == null) return;
        if (e.getClickedBlock().getType() == Material.ANVIL || e.getClickedBlock().getType() == Material.CRAFTING_TABLE) {
            e.setCancelled(true);
            if (!plugin.getPlayerMenuUtilityMap().containsKey(player.getUniqueId())) {
                plugin.getPlayerMenuUtilityMap().put(player.getUniqueId(), new PlayerMenuUtility(player));
            }
            PlayerMenuUtility playerMenuUtility = plugin.getPlayerMenuUtilityMap().get(player.getUniqueId());
            AnvilSelectionMenu menu = new AnvilSelectionMenu(plugin.getPlayerMenuUtilityMap().get(player.getUniqueId()), player, plugin);
            menu.open();
        }
    }
}
