package me.shufy.john.events.mlg;

import me.shufy.john.events.JohnEvent;
import me.shufy.john.util.JohnUtility;
import me.shufy.john.util.SoundInfo;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.concurrent.ThreadLocalRandom;

import static me.shufy.john.util.JohnUtility.bold;

public class MlgEventListener implements Listener {
    @EventHandler
    public void onDropItem (PlayerDropItemEvent e) {
        if (JohnUtility.loreContains(e.getItemDrop().getItemStack(), "The official MLG water bucket. Stop looking at this and land your mlg idiot")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(bold(ChatColor.RED) + "You can't drop this item");
        }
    }
    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent e) {
        if (JohnEvent.isEventInstanceRunning()) {
            if (JohnEvent.getEventInstanceName().equals("MlgEvent")) {
                if (!((MlgEvent) JohnEvent.getEventInstance()).deathList.contains(e.getEntity())) {
                    ((MlgEvent)JohnEvent.getEventInstance()).deathList.add(e.getEntity());
                    e.getEntity().getInventory().remove(Material.WATER_BUCKET);
                    e.getEntity().getInventory().remove(Material.BUCKET);
                    e.setDeathMessage(bold(ChatColor.GOLD) + e.getEntity().getName() + bold(ChatColor.RED) + " failed to " + bold(ChatColor.BLUE) + "MLG");
                    JohnEvent.getEventInstance().broadcastSound(Sound.ENTITY_ENDER_DRAGON_GROWL, new SoundInfo(1f, ThreadLocalRandom.current().nextFloat()));
                }
            }
        }
    }
}
