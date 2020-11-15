package me.shufy.john.corenpc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static me.shufy.john.util.JohnUtility.playerIsTargeting;

public class JohnListener implements Listener {
    @EventHandler
    public void onPlayerAttackJohn (PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR) {
            JohnNpc targetedNpc = null;
            for (JohnNpc johnNpc : JohnNpc.allNpcs) {
                if (playerIsTargeting(e.getPlayer(), johnNpc.getNpc().getBukkitEntity().getLocation().add(0, 1, 0))) {
                    targetedNpc = johnNpc;
                    break;
                }
            }
            // player did not attack any npc
            if (targetedNpc == null)
                return;
            // player probably did attack an npc
            if (targetedNpc.isSpawnedIn()) {
                targetedNpc.takeKnockback(e.getPlayer(), 2.1);
            }
        }
    }
}
