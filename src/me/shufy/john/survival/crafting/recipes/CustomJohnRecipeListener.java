package me.shufy.john.survival.crafting.recipes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomJohnRecipeListener implements Listener {

    public static List<JohnCustomRecipe> customRecipeList = new ArrayList<>();

    public static JohnCustomRecipe getRecipeFromMatrix(ItemStack[] matrix) {
        return customRecipeList.stream().filter(r -> Arrays.equals(r.getRecipeMatrix(), matrix)).findFirst().orElse(null);
    }

    public static boolean anyRecipeMatches(ItemStack[] matrix) {
        return customRecipeList.stream().anyMatch(r -> Arrays.equals(r.getRecipeMatrix(), matrix));
    }

    @EventHandler
    public void prepareCraftEvent (PrepareItemCraftEvent e) {
        if (anyRecipeMatches(e.getInventory().getMatrix())) {
            e.getInventory().setResult(getRecipeFromMatrix(e.getInventory().getMatrix()).getRecipeResult());
        }
    }
}
