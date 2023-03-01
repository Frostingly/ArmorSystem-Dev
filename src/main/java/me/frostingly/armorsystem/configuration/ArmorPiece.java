package me.frostingly.armorsystem.configuration;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class ArmorPiece {

    private final int health;
    private final int defense;
    private final int stamina;

    public ArmorPiece(int health, int defense, int stamina) {
        this.health = health;
        this.defense = defense;
        this.stamina = stamina;
    }

    public int getHealth() {
        return health;
    }

    public int getDefense() {
        return defense;
    }

    public int getStamina() {
        return stamina;
    }
}
