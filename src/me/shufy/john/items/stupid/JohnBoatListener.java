package me.shufy.john.items.stupid;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.logging.Level;

public class JohnBoatListener implements Listener {
    @EventHandler
    public void onBoatPlace(BlockPlaceEvent e) {
        Bukkit.getLogger().log(Level.INFO, e.getBlockPlaced().getMetadata("display name").toString());
    }
}
