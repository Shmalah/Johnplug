package me.shufy.john.survival.crafting.recipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class JohnCustomRecipe {
    public JohnCustomRecipe() {
        CustomJohnRecipeListener.customRecipeList.add(this);
    }
    public abstract String getRecipeName();
    public abstract ItemStack[] getRecipeMatrix();
    public abstract ItemStack getRecipeResult();

    // returns a new item stack from the given material, just a shortcut
    public ItemStack normalItem(Material material) {
        return new ItemStack(material);
    }
}
