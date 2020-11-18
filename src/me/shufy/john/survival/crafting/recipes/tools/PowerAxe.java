package me.shufy.john.survival.crafting.recipes.tools;

import me.shufy.john.survival.crafting.recipes.IngredientPair;
import me.shufy.john.survival.crafting.recipes.JohnRecipe;
import me.shufy.john.survival.items.tools.PowerAxeItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.Collection;

public class PowerAxe extends JohnRecipe {

    public PowerAxe() {
        super("PowerAxe");
    }

    @Override
    public ShapedRecipe recipeShape(ShapedRecipe currentRecipe) {
        return currentRecipe.shape("WW ", "WS ", " S ");
    }

    @Override
    public Collection<IngredientPair> recipeIngredients(ShapedRecipe recipeShape) {
        return new ArrayList<IngredientPair>() {{
            add(new IngredientPair('W', Material.ACACIA_LOG));
            add(new IngredientPair('S', Material.STICK));
        }};
    }

    @Override
    public ItemStack resultItem() {
        return new PowerAxeItem().getItemStack();
    }

}
