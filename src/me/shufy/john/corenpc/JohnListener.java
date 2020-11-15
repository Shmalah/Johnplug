package me.shufy.john.corenpc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class JohnListener implements Listener {
    @EventHandler
    public void onPlayerAttackJohn (PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (!e.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
                return;
            if (!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore())
                return;
            if (!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(0).contains("The John Deflector Rod"))
                return;
            for (JohnNpc johnNpc : JohnNpc.allNpcs) {
                Vector vLook = johnNpc.npc.getBukkitEntity().getBoundingBox().getCenter().subtract(e.getPlayer().getEyeLocation().toVector());
                RayTraceResult rt = e.getPlayer().getWorld().rayTraceEntities(e.getPlayer().getEyeLocation(), vLook, 5.0);
                if (rt != null) {
                    if (johnNpc.npc.getBukkitEntity().getBoundingBox().getCenter().subtract(rt.getHitPosition()).length() < 1.1) {
                        johnNpc.takeKnockback(e.getPlayer(), 2.1);
                    }
                }
            }
        }
    }
}
