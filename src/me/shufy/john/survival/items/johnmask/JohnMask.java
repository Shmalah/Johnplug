package me.shufy.john.survival.items.johnmask;

import me.shufy.john.general.item.GeneralJohnItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static me.shufy.john.util.JohnUtility.bold;

public class JohnMask extends GeneralJohnItem {
    @Override
    public ItemStack getItemStack() {
        ItemStack mask = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta maskMeta = mask.getItemMeta();
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
}
