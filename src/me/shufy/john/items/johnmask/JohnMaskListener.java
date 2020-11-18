package me.shufy.john.items.johnmask;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;

import static me.shufy.john.util.JohnUtility.bold;
import static me.shufy.john.util.JohnUtility.loreContains;

public class JohnMaskListener implements Listener {

    public static boolean hasMaskOn(Player player) {
        return Arrays.stream(player.getInventory().getArmorContents()).anyMatch(item -> loreContains(item, "The John Mask"));
    }

    @EventHandler
    public void equipMask (InventoryClickEvent e) {

        // putting pumpkin INTO helmet slot
        if (e.getCurrentItem() != null) {
            if (e.getCurrentItem().getType().equals(Material.AIR)) {
                if (e.getCursor() != null) {
                    if (e.getCursor().getType().equals(Material.CARVED_PUMPKIN)) {
                        if (loreContains(e.getCursor(), "The John Mask")) {
                            if (e.getSlot() == 39 && e.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
                                // they've put on the john mask
                                Player player = (CraftPlayer)e.getWhoClicked();
                                player.sendMessage(bold(ChatColor.DARK_AQUA) + "You've equipped " + bold(ChatColor.GOLD) + "The John Mask");
                            }
                        }
                    }
                }
            }
        }

//        Bukkit.getLogger().log(Level.INFO, "Current Item: " + e.getCurrentItem());
//        Bukkit.getLogger().log(Level.INFO, "Cursor: " + e.getCursor());
//        Bukkit.getLogger().log(Level.INFO, "Click Name: " + e.getClick().name());
    }
}
