package me.shufy.john.survival.crafting.recipes.john;

import me.shufy.john.survival.crafting.recipes.JohnCustomRecipe;
import me.shufy.john.survival.items.common.wood.JohnWood;
import org.bukkit.inventory.ItemStack;

public class JohnBoat extends JohnCustomRecipe {
    @Override
    public String getRecipeName() {
        return "JohnBoat";
    }

    @Override
    public ItemStack[] getRecipeMatrix() {
        ItemStack johnWood = new JohnWood().getItemStack();
        return new ItemStack[] {
                null, null, null,
                johnWood, null, johnWood,
                johnWood, johnWood, johnWood
        };
    }

    @Override
    public ItemStack getRecipeResult() {
        return new me.shufy.john.survival.items.stupid.boat.JohnBoat().getItemStack();
    }
}
