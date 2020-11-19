package me.shufy.john.survival.crafting.recipes.john;

import me.shufy.john.survival.crafting.recipes.IngredientPair;
import me.shufy.john.survival.crafting.recipes.IngredientPairList;
import me.shufy.john.survival.crafting.recipes.JohnRecipe;
import me.shufy.john.survival.items.johnmask.JohnMaskItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class JohnMask extends JohnRecipe {

    public JohnMask(String recipeName) {
        super(recipeName);
    }

    @Override
    public ShapedRecipe recipeShape(ShapedRecipe currentRecipe) {
        return currentRecipe.shape(" E ", "EPE", " E ");
    }

    @Override
    public IngredientPairList recipeIngredients(ShapedRecipe recipeShape) {
        return new IngredientPairList(
                new IngredientPair('E', Material.EMERALD),
                new IngredientPair('P', Material.CARVED_PUMPKIN)
        );
    }

    @Override
    public ItemStack resultItem() {
        return new JohnMaskItem().getItemStack();
    }

}
