package me.frostingly.armorsystem;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> format(List<String> list) {
        List<String> newList = new ArrayList<>();
        for (String string : list) {
            newList.add(format(string));
        }
        return newList;
    }

    public static int round (int value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (int) Math.round(value * scale) / scale;
    }

    public static <T, E> Set<T> getKeysByValue(Map<Integer, Map<Integer, ItemStack>> map, Map<Integer, ItemStack> value) {
        return (Set<T>) map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

}
