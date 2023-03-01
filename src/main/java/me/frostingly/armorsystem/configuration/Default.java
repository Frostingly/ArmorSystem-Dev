package me.frostingly.armorsystem.configuration;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;

public class Default {
    private static Map<Material, Integer> itemID = new HashMap<>();

    public void loadValues() {
        int index = 10000;
        for (Material material : Material.values()) {
            System.out.println(index);
            itemID.put(material, Integer.valueOf(index));
            index++;
        }
    }

    public static Map<Material, Integer> getItemID() {
        return itemID;
    }
}
