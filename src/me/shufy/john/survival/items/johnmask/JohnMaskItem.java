package me.shufy.john.survival.items.johnmask;

import me.shufy.john.general.item.GeneralJohnItem;
import me.shufy.john.util.john.JohnUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static me.shufy.john.util.john.JohnUtility.bold;

public class JohnMaskItem extends GeneralJohnItem {

    // TODO make random-john behave differently when a user is wearing the john mask
    // TODO make john mask break eventually

    @Override
    public ItemStack getItemStack() {
        ItemStack mask = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta maskMeta = mask.getItemMeta();
        assert maskMeta != null;
        maskMeta.setDisplayName(bold(ChatColor.LIGHT_PURPLE) + "John Mask");
        maskMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        maskMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        maskMeta.setLore(new ArrayList<String>() {{
            add (bold(ChatColor.GOLD) + "The John Mask -- Protect yourself from John.. At least for now.");
            add (ChatColor.GOLD + "Ineffective at non-random john appearances. E.g. losing an event");
        }});
        mask.setItemMeta(maskMeta);
        return mask;
    }

    @Override
    public boolean equals(ItemStack other) {
        return JohnUtility.loreContains(other, "The John Mask -- Protect yourself from John");
    }
}
