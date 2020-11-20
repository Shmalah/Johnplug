package me.shufy.john.survival.crafting.recipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

// for usage with class JohnRecipe..

public interface JohnCraftableRecipe {

    /* example usage */

    default ShapedRecipe recipeShape(ShapedRecipe currentRecipe) {
        return currentRecipe.shape("123", "456", "789");
    }

    default IngredientPairList recipeIngredients(ShapedRecipe recipeShape) {
        return new IngredientPairList(
                new IngredientPair('1', Material.GOLDEN_HOE),
                new IngredientPair('2', Material.DIRT)
        );
    }

    default ItemStack resultItem() {
        return new ItemStack(Material.COARSE_DIRT);
    }

}
