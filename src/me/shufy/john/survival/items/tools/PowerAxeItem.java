package me.shufy.john.survival.items.tools;

import me.shufy.john.general.item.GeneralJohnItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static me.shufy.john.util.john.JohnUtility.bold;

public class PowerAxeItem extends GeneralJohnItem {
    @Override
    public ItemStack getItemStack() {
        ItemStack powerAxe = new ItemStack(Material.WOODEN_AXE);
        ItemMeta powerAxeMeta = powerAxe.getItemMeta();
        assert powerAxeMeta != null;
        powerAxeMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        powerAxeMeta.addEnchant(Enchantment.MENDING, 1, true);
        powerAxeMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
        powerAxeMeta.setDisplayName(bold(ChatColor.LIGHT_PURPLE) + "POWER AXE");
        powerAxeMeta.setLore(new ArrayList<String>() {{
            add (ChatColor.GOLD + "Your very own John-inspired Power Axe.");
            add (ChatColor.GOLD + "It's busted. Abuse it. Make them cry.");
        }});
        powerAxe.setItemMeta(powerAxeMeta);
        return powerAxe;
    }
}
