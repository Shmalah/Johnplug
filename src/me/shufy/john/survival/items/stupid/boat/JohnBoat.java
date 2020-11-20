package me.shufy.john.survival.items.stupid.boat;

import me.shufy.john.general.item.GeneralJohnItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static me.shufy.john.util.john.JohnUtility.bold;

public class JohnBoat extends GeneralJohnItem {
    @Override
    public ItemStack getItemStack() {
        return johnBoat();
    }
    public static ItemStack johnBoat() {
        ItemStack boat = new ItemStack(Material.ACACIA_BOAT);
        ItemMeta meta = boat.getItemMeta();
        assert meta != null;
        meta.setLocalizedName(bold(ChatColor.RED) + "JOHN BOAT");
        meta.setDisplayName(bold(ChatColor.RED) + "JOHN BOAT");
        meta.setLore(new ArrayList<String>(){{
            add(ChatColor.GOLD + "The john boat!");
        }});
        meta.addEnchant(Enchantment.KNOCKBACK, 20, true);
        boat.setItemMeta(meta);
        return boat;
    }
}
