package me.shufy.john.survival.items.egg;

import me.shufy.john.general.item.GeneralJohnItem;
import me.shufy.john.util.john.JohnUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static me.shufy.john.util.john.JohnUtility.bold;

public class JohnEgg extends GeneralJohnItem {
    @Override
    public ItemStack getItemStack() {
        ItemStack egg = new ItemStack(Material.EGG);
        ItemMeta eggMeta = egg.getItemMeta();
        eggMeta.setDisplayName(bold(ChatColor.GRAY) + "John's Egg");
        eggMeta.setLore(new ArrayList<String>() {{
            add(bold(ChatColor.GOLD) + "John's lovely egg.");
        }});
        eggMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, true);
        egg.setItemMeta(eggMeta);
        return egg;
    }

    @Override
    public boolean equals(ItemStack other) {
        return JohnUtility.loreContains(other, "John's lovely egg.");
    }
}
