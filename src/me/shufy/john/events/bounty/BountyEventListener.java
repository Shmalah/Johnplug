package me.shufy.john.events.bounty;

import me.shufy.john.Main;
import me.shufy.john.util.JohnUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.logging.Level;

public class BountyEventListener implements Listener {

    public static final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onDeath (PlayerDeathEvent e) {
        if (BountyEvent.isInstanceRunning()) {
            BountyEvent runningBounty = BountyEvent.getRunningInstance();
            if (e.getEntity().equals(runningBounty.target) && !runningBounty.hasKilled) {
                Bukkit.getLogger().log(Level.INFO, "Target died to the hand of the hunter! Mission complete for hunter.");
                runningBounty.hasKilled = true;
            }
        }
    }

    boolean johnLoggerRunning = false;

    UUID savedHunterUuid = null;

    @EventHandler
    public void hunterLeave (PlayerQuitEvent e) {
        if (BountyEvent.isInstanceRunning()) {
            BountyEvent runningBounty = BountyEvent.getRunningInstance();
            if (e.getPlayer().getUniqueId().equals(runningBounty.getHunter().getUniqueId())) {
                Bukkit.getLogger().log(Level.INFO, "Saving hunter " + e.getPlayer().getName() + "(" + e.getPlayer().getUniqueId().toString() + ") cause they left ");
                savedHunterUuid = e.getPlayer().getUniqueId();
            }
        }
    }

    @EventHandler
    public void hunterJoinBack (PlayerJoinEvent e) {
        if (BountyEvent.isInstanceRunning()) {
            BountyEvent runningBounty = BountyEvent.getRunningInstance();
            if (savedHunterUuid != null && e.getPlayer().getUniqueId().equals(savedHunterUuid)) {
                Bukkit.getLogger().log(Level.INFO, "Restoring hunter " + e.getPlayer().getName() + "(" + e.getPlayer().getUniqueId().toString() + ") cause they joined back and matched UUID \"" + savedHunterUuid.toString() + "\"");
                runningBounty.hunter = e.getPlayer();
                savedHunterUuid = null;
            }
        }
    }

    @EventHandler
    public void onPlayerDamage (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            // PVP is happening
            Player damager = (Player) e.getDamager(), player = (Player) e.getEntity();
            if (BountyEvent.isInstanceRunning()) {
                // there is an active bounty running
                BountyEvent runningBounty = BountyEvent.getRunningInstance();
                if (damager.equals(runningBounty.hunter) && player.equals(runningBounty.target)) {
                    // the hunter just damaged the target
                    if (johnLoggerRunning)
                        return;
                    Bukkit.getLogger().log(Level.INFO, "John combat log for bounty event has been triggered! 10 seconds left.");
                    johnLoggerRunning = true;
                    new BukkitRunnable() {
                        int ticks = 0;
                        @Override
                        public void run() {
                            // prevent john logging
                            if (!player.isOnline()) {
                                JohnUtility.johnBan(player, 60);
                                runningBounty.hasKilled = true;
                                Bukkit.getLogger().log(Level.INFO, "Target john logged, Hunter automatically won and target banned.");
                                this.cancel();
                            }
                            ticks++;
                            if (ticks >= 200) {
                                johnLoggerRunning = false;
                                Bukkit.getLogger().log(Level.INFO, "John combat log for bounty event has been released.");
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 0, 1L);
                }
            }
        }
    }
}
