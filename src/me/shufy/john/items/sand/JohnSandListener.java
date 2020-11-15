package me.shufy.john.items.sand;

import me.shufy.john.util.JohnUtility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class JohnSandListener implements Listener {
    @EventHandler
    public void blockPlace (BlockPlaceEvent e) {
        if (e.getItemInHand().hasItemMeta())
            if (e.getItemInHand().getItemMeta().hasLore())
                if (e.getItemInHand().getItemMeta().getLore().get(0).contains("John's beloved sand.")) {
                    e.getBlockPlaced().setType(JohnUtility.randomMaterialWhoContains("sand"));
                }
    }
}
