package me.shufy.john.survival.crafting.recipes.tools;

import me.shufy.john.survival.crafting.recipes.IngredientPair;
import me.shufy.john.survival.crafting.recipes.IngredientPairList;
import me.shufy.john.survival.crafting.recipes.JohnRecipe;
import me.shufy.john.survival.items.tools.PowerAxeItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class PowerAxe extends JohnRecipe {

    public PowerAxe() {
        super("PowerAxe");
    }

    @Override
    public ShapedRecipe recipeShape(ShapedRecipe currentRecipe) {
        return currentRecipe.shape("WW ", "WS ", " S ");
    }

    @Override
    public IngredientPairList recipeIngredients(ShapedRecipe recipeShape) {
        return new IngredientPairList(
                new IngredientPair('W', Material.ACACIA_LOG),
                new IngredientPair('S', Material.STICK)
        );
    }

    @Override
    public ItemStack resultItem() {
        return new PowerAxeItem().getItemStack();
    }

}
