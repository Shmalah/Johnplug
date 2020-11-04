package me.shufy.john.items.stupid;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static me.shufy.john.util.JohnUtility.bold;

public class JohnBoat {
    public JohnBoat() {

    }
    public static ItemStack johnBoat() {
        ItemStack boat = new ItemStack(Material.ACACIA_BOAT);
        ItemMeta meta = boat.getItemMeta();
        assert meta != null;
        meta.setDisplayName(bold(ChatColor.RED) + "JOHN BOAT");
        meta.setLore(new ArrayList<String>(){{
            add(ChatColor.GOLD + "The john boat!");
        }});
        meta.addEnchant(Enchantment.KNOCKBACK, 20, true);
        boat.setItemMeta(meta);
        return boat;
    }
}
