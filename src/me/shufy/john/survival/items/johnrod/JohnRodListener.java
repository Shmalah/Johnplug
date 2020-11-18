package me.shufy.john.survival.items.johnrod;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.concurrent.ThreadLocalRandom;


public class JohnRodListener implements Listener {
    // PLAYER OBTAINING JOHN ROD
    @EventHandler
    public void playerFish (PlayerFishEvent e) {
        if (ThreadLocalRandom.current().nextDouble() < 0.10d) {
            if (e.getState().equals(PlayerFishEvent.State.REEL_IN) && e.getHook().getLocation().getBlock().getType().equals(Material.WATER)) {
                JohnRod johnRod = new JohnRod();
                e.getPlayer().getInventory().addItem(johnRod.getItemStack());
            }
        }
    }
}
