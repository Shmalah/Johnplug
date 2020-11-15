package me.shufy.john.items.johnrod;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static me.shufy.john.util.JohnUtility.bold;

public class JohnRod {
    ItemStack johnRod;
    public JohnRod() {
        this.johnRod = johnRodItem();
    }
    private ItemStack johnRodItem() {
        ItemStack johnRod = new ItemStack(Material.FISHING_ROD);
        ItemMeta johnRodMeta = johnRod.getItemMeta();
        johnRodMeta.setDisplayName(bold(ChatColor.LIGHT_PURPLE) + "The John Deflector Rod");
        johnRodMeta.setLore(new ArrayList<String>() {{
            add(ChatColor.GOLD + "The John Deflector Rod -- Your only hope.");
        }});
        johnRodMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        johnRod.setItemMeta(johnRodMeta);
        return johnRod;
    }
    public ItemStack getItemStack() {
        return johnRod;
    }
}
