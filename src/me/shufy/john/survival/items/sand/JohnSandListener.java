package me.shufy.john.survival.items.sand;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.concurrent.ThreadLocalRandom;

import static me.shufy.john.util.john.JohnUtility.loreContains;
import static me.shufy.john.util.john.JohnUtility.randomMaterialWhoContains;

public class JohnSandListener implements Listener {
    // WHEN PLAYER USES THE SAND
    @EventHandler
    public void blockPlace (BlockPlaceEvent e) {
        if (loreContains(e.getPlayer().getInventory().getItemInMainHand(), "John's beloved sand.")) {
            e.getBlockPlaced().setType(randomMaterialWhoContains("SAND"));
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
