package me.shufy.john.survival.items.sand;

import me.shufy.john.general.item.GeneralJohnItem;
import me.shufy.john.util.john.JohnUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static me.shufy.john.util.john.JohnUtility.bold;

public class JohnSand extends GeneralJohnItem {
    public ItemStack getItemStack() {
        ItemStack sand = new ItemStack(Material.SAND);
        ItemMeta sandMeta = sand.getItemMeta();
        sandMeta.setDisplayName(bold(ChatColor.WHITE) + "John's Sand");
        sandMeta.setLore(new ArrayList<String>() {{
            add(bold(ChatColor.GOLD) + "John's beloved sand.");
        }});
        sand.setItemMeta(sandMeta);
        return sand;
    }

    @Override
    public boolean equals(ItemStack other) {
        return JohnUtility.loreContains(other, "John's beloved sand.");
    }
}
