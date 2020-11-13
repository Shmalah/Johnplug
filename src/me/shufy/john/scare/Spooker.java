package me.shufy.john.scare;

import me.shufy.john.DebugCommands;
import me.shufy.john.Main;
import me.shufy.john.util.JohnUtility;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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
                if (ThreadLocalRandom.current().nextDouble() < 0.2d || DebugCommands.isDebugMode()) {
                    spookPlayer(JohnUtility.randomPlayer(JohnUtility.getWorldWithMostPlayers()), ThreadLocalRandom.current().nextInt(20, 120));
                }
            }
        }.runTaskTimer(plugin, 0, (20L * 10));
    }
    private void spookPlayer(Player player, int johnExistenceTicks) {

        // john should only appear at night
        if (player.getWorld().getTime() < 14000)
            return;

        Npc npc = new Npc();
        Sound randomMusicSound = JohnUtility.randomSoundWhoContains("music");

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, johnExistenceTicks+2, 6));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, johnExistenceTicks+15, 3));
        player.playSound(npc.getNpcPlayer().getBukkitEntity().getEyeLocation(), randomMusicSound, 1f, 0.1f);

        npc.getNpcPlayer().setCustomNameVisible(false);
        npc.appearTo(player);

        // chance that john will force the victim to look at him, freezing the player in time.
        boolean forceLook = (ThreadLocalRandom.current().nextDouble() < 0.2d);

        // set player to look at john at least on the first tick so that he can appear "in front" of them
        player.teleport(player.getLocation().setDirection(npc.getNpcPlayer().getBukkitEntity().getLocation().toVector().subtract(player.getLocation().toVector()).normalize()));

        // responsible for controlling john's gaze
        BukkitTask lookTask = new BukkitRunnable() {
            @Override
            public void run() {
                npc.lookAt(player);
                if (forceLook)
                    player.teleport(player.getLocation().setDirection(npc.getNpcPlayer().getBukkitEntity().getLocation().toVector().subtract(player.getLocation().toVector()).normalize()));
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 1L);

        // responsible for ending the spook
        new BukkitRunnable() {
            @Override
            public void run() {
                npc.remove(player);
                player.stopSound(randomMusicSound);
                lookTask.cancel();
            }
        }.runTaskLater(Main.getPlugin(Main.class), johnExistenceTicks);
    }
}
