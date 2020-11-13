package me.shufy.john.items.stupid.johnskull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import static me.shufy.john.util.JohnUtility.JOHN_UUID;
import static me.shufy.john.util.JohnUtility.bold;

public class JohnSkull {
    public JohnSkull() {

    }
    public static ItemStack johnSkull() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        OfflinePlayer johnPlayer = Bukkit.getOfflinePlayer(JOHN_UUID);
        meta.setDisplayName(bold(ChatColor.RED) + "JOHN SKULL");
        meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        meta.setOwningPlayer(johnPlayer);
        head.setItemMeta(meta);
        return head;
    }
}
