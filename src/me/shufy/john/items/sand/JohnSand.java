package me.shufy.john.items.sand;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static me.shufy.john.util.JohnUtility.bold;

public class JohnSand {
    ItemStack sand;
    public JohnSand() {
        this.sand = johnSand();
    }
    private ItemStack johnSand() {
        sand = new ItemStack(Material.SAND);
        ItemMeta sandMeta = sand.getItemMeta();
        sandMeta.setDisplayName(bold(ChatColor.WHITE) + "John's Sand");
        sandMeta.setLore(new ArrayList<String>() {{
            add(bold(ChatColor.GOLD) + "John's beloved sand.");
        }});
        sand.setItemMeta(sandMeta);
        return sand;
    }
    public ItemStack getItemStack() {
        return this.sand;
    }
}
