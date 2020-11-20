package me.shufy.john.survival.items.common.wood;

import me.shufy.john.general.item.GeneralJohnItem;
import me.shufy.john.util.john.JohnUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static me.shufy.john.util.john.JohnUtility.bold;

public class JohnWood extends GeneralJohnItem {
    @Override
    public ItemStack getItemStack() {
        ItemStack johnWood = new ItemStack(Material.ACACIA_LOG);
        ItemMeta johnWoodMeta = johnWood.getItemMeta();
        assert johnWoodMeta != null;
        johnWoodMeta.setDisplayName(bold(ChatColor.LIGHT_PURPLE) + "John's Wood");
        johnWoodMeta.setLore(new ArrayList<String>() {{
            add(ChatColor.GOLD + "John's beloved wood. Used to craft some john items or somethn.");
        }});
        johnWoodMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        johnWood.setItemMeta(johnWoodMeta);
        return johnWood;
    }

    @Override
    public boolean equals(ItemStack other) {
        return JohnUtility.loreContains(other, "John's beloved wood.");
    }
}
