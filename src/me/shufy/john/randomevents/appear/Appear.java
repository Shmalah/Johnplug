package me.shufy.john.randomevents.appear;

import me.shufy.john.items.JohnItem;
import me.shufy.john.items.JohnItemListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static me.shufy.john.util.JohnUtility.*;

public class Appear  {
    boolean isRunning = false;
    int ticksToAppearFor;
    String targetName;
    Location appearLocation;
    public Appear() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (randomChance(0.05d)) {
                    if (!isRunning) {
                        isRunning = true;
                        Player target = null;
                        for (World world : Bukkit.getWorlds()) {
                            if (!world.getPlayers().isEmpty()) {
                                target = randomPlayer(world);
                                beforeAppearance(target);
                                break;
                            }
                        }
                        if (target != null)
                            targetName = target.getName();
                            Bukkit.getLogger().log(Level.INFO, "Target: " + target.getName());
                        Collection<Player> playersInSurvival = new ArrayList<>(Bukkit.getOnlinePlayers()).stream().filter(p -> !p.getGameMode().equals(GameMode.CREATIVE)).collect(Collectors.toList());
                        if (!playersInSurvival.isEmpty()) {
                            ticksToAppearFor = randomInt(20, 70);
                            if (target == null) {
                                Bukkit.getLogger().log(Level.FINE, String.format("player \"%s\" died or left the server before the event could happen", targetName));
                                return; // player died or left the server before the event could happen..
                            }
                            Location randomLocation = new Location(target.getWorld(),
                                    target.getLocation().getX() + randomInt(10), target.getLocation().getY(), target.getLocation().getZ() + randomInt(10));
                            randomLocation.setY(target.getWorld().getHighestBlockYAt(randomLocation.getBlockX(), randomLocation.getBlockZ()));
                            appearLocation = randomLocation;
                            duringAppearance(appearLocation);
                        }
                        if (target != null) {
                            Player finalTarget = target; // target is final
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (finalTarget.isDead() || !finalTarget.isOnline()) {
                                        afterAppearance(null);
                                    } else {
                                        afterAppearance(finalTarget.getLocation());
                                    }
                                    isRunning = false;
                                }
                            }.runTaskLater(JohnItemListener.plugin, ticksToAppearFor);
                        }
                    }
                }
            }
        }.runTaskTimer(JohnItemListener.plugin, 0, 200L);
    }


    public void beforeAppearance(Player appearTo) {

    }


    public void duringAppearance(Location appearLocation) {

    }


    public void afterAppearance(Location playerLocation) {

    }
}
