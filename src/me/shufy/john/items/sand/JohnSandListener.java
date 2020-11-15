package me.shufy.john.items.sand;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.ThreadLocalRandom;

public class JohnSandListener implements Listener {
    // WHEN PLAYER USES THE SAND
    @EventHandler
    public void blockPlace (PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR))
        if (e.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
            if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore())
                if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(0).contains("The John Deflector Rod")) {

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
