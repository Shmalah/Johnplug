package me.shufy.john.survival.items.common.wood;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static me.shufy.john.util.john.JohnUtility.randomChanceNoDebug;

public class JohnWoodListener implements Listener {
    @EventHandler
    public void blockBreak (BlockBreakEvent e) {
        if (e.getBlock().getType().name().contains("LOG")) {
            if (randomChanceNoDebug(0.05d)) {
                e.setDropItems(false);
                e.getPlayer().getWorld().dropItem(e.getBlock().getLocation(), new JohnWood().getItemStack());
            }
        }
    }
}
