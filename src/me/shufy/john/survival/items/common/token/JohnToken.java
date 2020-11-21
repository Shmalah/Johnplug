package me.shufy.john.survival.items.common.token;

import me.shufy.john.survival.crafting.recipes.JohnCustomRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class JohnToken extends JohnCustomRecipe {
    @Override
    public String getRecipeName() {
        return "JohnToken";
    }

    @Override
    public ItemStack[] getRecipeMatrix() {
        ItemStack emerald = normalItem(Material.EMERALD);
        ItemStack ghastTear = normalItem(Material.GHAST_TEAR);
        return new ItemStack[] {
            null, emerald, null,
            emerald, ghastTear, emerald,
            null, emerald, null
        };
    }

    @Override
    public ItemStack getRecipeResult() {
        return null;
    }
}
