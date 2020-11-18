package me.shufy.john.survival.crafting.recipes;

import org.bukkit.Material;

public class IngredientPair {
    public char c;
    public Material ingredient;
    public IngredientPair(char c, Material ingredient) {
        this.c = c;
        this.ingredient = ingredient;
    }
}
