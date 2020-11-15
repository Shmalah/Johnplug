package me.shufy.john.items.sand;

import me.shufy.john.util.JohnUtility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.concurrent.ThreadLocalRandom;

public class JohnSandListener implements Listener {
    // WHEN PLAYER USES THE SAND
    @EventHandler
    public void blockPlace (BlockPlaceEvent e) {
        if (e.getItemInHand().hasItemMeta())
            if (e.getItemInHand().getItemMeta().hasLore())
                if (e.getItemInHand().getItemMeta().getLore().get(0).contains("John's beloved sand.")) {
                    e.getBlockPlaced().setType(JohnUtility.randomMaterialWhoContains("sand"));
                }
    }

    // PLAYER OBTAINING THE SAND
    @EventHandler
    public void blockMine (BlockBreakEvent e) {
        if (e.getBlock().getType().name().contains("SAND"))
        if (ThreadLocalRandom.current().nextDouble() < 0.1d) {
            e.setDropItems(false);
            JohnSand johnSand = new JohnSand();
            e.getPlayer().getWorld().dropItem(e.getBlock().getLocation(), johnSand.getItemStack());
        }
    }
}
