package me.shufy.john.survival.items.johnrod;

import me.shufy.john.general.item.GeneralJohnItem;
import me.shufy.john.util.john.JohnUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static me.shufy.john.util.john.JohnUtility.bold;

public class JohnRod extends GeneralJohnItem {
    public ItemStack getItemStack() {
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

    @Override
    public boolean equals(ItemStack other) {
        return JohnUtility.loreContains(other, "The John Deflector Rod");
    }
}
