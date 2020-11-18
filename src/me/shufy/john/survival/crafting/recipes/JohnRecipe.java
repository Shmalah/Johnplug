package me.shufy.john.survival.crafting.recipes;

import me.shufy.john.Main;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Collection;

public abstract class JohnRecipe {
    String recipeName;
    public JohnRecipe(String recipeName) {
        this.recipeName = recipeName;
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), recipeName.replace(" ", "_"));
        ShapedRecipe recipe = recipeShape(new ShapedRecipe(key, resultItem()));
        for (IngredientPair recipeIngredient : recipeIngredients(recipe))
            recipe.setIngredient(recipeIngredient.c, recipeIngredient.ingredient);
        Bukkit.addRecipe(recipe);
    }
    public abstract ShapedRecipe recipeShape(ShapedRecipe currentRecipe);
    public abstract Collection<IngredientPair> recipeIngredients(ShapedRecipe recipeShape);
    public abstract ItemStack resultItem();
}
