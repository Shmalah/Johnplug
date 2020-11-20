package me.shufy.john.survival.crafting.recipes;

import org.bukkit.inventory.ItemStack;

public abstract class JohnCustomRecipe {
    public JohnCustomRecipe() {
        CustomJohnRecipeListener.customRecipeList.add(this);
    }
    public abstract String getRecipeName();
    public abstract ItemStack[] getRecipeMatrix();
    public abstract ItemStack getRecipeResult();
}
