package me.shufy.john.scare;

import me.shufy.john.DebugCommands;
import me.shufy.john.Main;
import me.shufy.john.util.JohnUtility;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ThreadLocalRandom;

import static me.shufy.john.util.JohnUtility.randomLocationNearPlayer;

public class SpookerStorm {
    public static final Main plugin = Main.getPlugin(Main.class);
    public static boolean stormOngoing = false;
    public SpookerStorm() {
        spookerStormRunner();
    }
    private void spookerStormRunner() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ThreadLocalRandom.current().nextDouble() < 0.1d || DebugCommands.isDebugMode()) {
                    doSpookyStorm(JohnUtility.randomPlayer(JohnUtility.getWorldWithMostPlayers()), ThreadLocalRandom.current().nextInt(20, 120));
                }
            }
        }.runTaskTimer(plugin, 0, (20L * 10));
    }
    private void doSpookyStorm(Player player, int duration) {
        // stormOngoing = spooky storm is already in progress.. there cant be 2 at one time
        if (!player.getWorld().hasStorm() && stormOngoing)
            return;

        stormOngoing = true;

        // make sure it rains during the entire duration of the john storm..
        player.getWorld().setThunderDuration(duration);

        // scary ambience/sounds/music
        for (Player worldPlayer : player.getWorld().getPlayers()) {
            worldPlayer.playSound(randomLocationNearPlayer(worldPlayer, 10), Sound.AMBIENT_BASALT_DELTAS_LOOP, 1f, 0.5f);
            worldPlayer.playSound(randomLocationNearPlayer(worldPlayer, 10), Sound.MUSIC_MENU, 1f, 0.1f);
        }

        // makes time move 10x faster (JOHN TIME !!!)
        BukkitTask fastDayCycleTask = new BukkitRunnable() {
            @Override
            public void run() {
                player.getWorld().setTime(player.getWorld().getTime() + 10L); // time moves 10x faster
            }
        }.runTaskTimer(plugin, 0, 6L);

        // makes storm scary
        BukkitTask scaryStormTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (ThreadLocalRandom.current().nextDouble() < 0.4d) {
                    Location lightningLoc = randomLocationNearPlayer(player, 50);
                    player.getWorld().strikeLightningEffect(lightningLoc);
                }
            }
        }.runTaskTimer(plugin, 0, 10L);

        // ends the spooky storm after int duration and activates spooky storm cooldown (see runnable inside)
        new BukkitRunnable() {
            @Override
            public void run() {
                player.getWorld().setStorm(false);
                player.getWorld().setThundering(false);
                fastDayCycleTask.cancel();
                scaryStormTask.cancel();
                for (Player worldPlayer : player.getWorld().getPlayers()) {
                    worldPlayer.stopSound(Sound.AMBIENT_BASALT_DELTAS_LOOP);
                    worldPlayer.stopSound(Sound.MUSIC_MENU);
                }
                // make it so that there wont be another scary storm for AT LEAST another 2 minutes..
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        stormOngoing = false;
                    }
                }.runTaskLater(plugin, (20L * ThreadLocalRandom.current().nextInt(120, 1000)));
            }
        }.runTaskLater(plugin, duration);

    }
}
