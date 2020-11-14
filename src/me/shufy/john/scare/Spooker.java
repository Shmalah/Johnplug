package me.shufy.john.scare;

import me.shufy.john.DebugCommands;
import me.shufy.john.Main;
import me.shufy.john.util.JohnUtility;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class Spooker {
    public static final Main plugin = Main.getPlugin(Main.class);
    public Spooker() {
        spookyRunner();
    }
    private void spookyRunner() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ThreadLocalRandom.current().nextDouble() < 0.05d || DebugCommands.isDebugMode()) {
                    spookPlayer(JohnUtility.randomPlayer(JohnUtility.getWorldWithMostPlayers()), ThreadLocalRandom.current().nextInt(20, 120));
                }
            }
        }.runTaskTimer(plugin, 0, (20L * 10));
    }
    private void spookPlayer(Player player, int johnExistenceTicks) {

        // john should only appear at night and when there's players online
        if (Bukkit.getOnlinePlayers().isEmpty())
            return;
        if (player.getWorld().getTime() < 14000)
            return;

        Npc npc = new Npc();
        Sound randomMusicSound = JohnUtility.randomSoundWhoContains("music");

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, johnExistenceTicks+2, 6));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, johnExistenceTicks+15, 3));
        if (ThreadLocalRandom.current().nextDouble() < 0.2d)
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, johnExistenceTicks+10, 3));
        if (ThreadLocalRandom.current().nextDouble() > 0.8d)
            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, johnExistenceTicks+10, 2));
        player.playSound(npc.getNpcPlayer().getBukkitEntity().getEyeLocation(), randomMusicSound, 1f, 0.1f);

        npc.getNpcPlayer().setCustomNameVisible(false);
        npc.appearTo(player);

        // responsible for controlling john's gaze
        BukkitTask lookTask = new BukkitRunnable() {
            @Override
            public void run() {
                npc.lookAt(player);
               // player.teleport(new Location(player.getWorld(), playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                player.teleport(player.getLocation().setDirection(npc.getNpcPlayer().getBukkitEntity().getLocation().toVector().subtract(player.getLocation().toVector()).normalize()));
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 1L);

        // responsible for ending the spook
        new BukkitRunnable() {
            @Override
            public void run() {
                npc.remove(player);
                player.stopSound(randomMusicSound);
                player.setVelocity(player.getLocation().getDirection().multiply(-5).add(new Vector(0, 2, 0)));
                lookTask.cancel();
            }
        }.runTaskLater(Main.getPlugin(Main.class), johnExistenceTicks);
    }
}
