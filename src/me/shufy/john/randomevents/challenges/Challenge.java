package me.shufy.john.randomevents.challenges;

import me.shufy.john.Main;
import me.shufy.john.randomevents.npc.JohnChallenge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;

import static me.shufy.john.util.JohnUtility.bold;
import static me.shufy.john.util.JohnUtility.randomChance;

public class Challenge implements JohnChallenge {

    public static final Main plugin = Main.getPlugin(Main.class);
    static boolean challengeAlreadyRunning = false;
    public Collection<Player> players;
    int eventDuration = 60; // default

    public Challenge() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (randomChance(0.10d)) {
                    if (!challengeAlreadyRunning) {
                        challengeAlreadyRunning = true;
                        players = new ArrayList<>(Bukkit.getOnlinePlayers());
                        countdown();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20L);
    }

    public void countdown() {
        new BukkitRunnable() {
            private int countdownTimer = 10;
            @Override
            public void run() {
                if (countdownTimer == 0) {
                    startEvent();
                    this.cancel();
                }
                countdownTimer--;
            }
        }.runTaskTimer(plugin, 0, 20L);
    }

    public void startEvent() {
        new BukkitRunnable() {
            int secondsLeft = eventDuration;
            @Override
            public void run() {

            }
            private void countdown() {
                if ((secondsLeft % 15 == 0 && secondsLeft != 0) || secondsLeft <= 10) {
                    players.forEach(player -> {
                        player.sendMessage(bold(ChatColor.GOLD) + secondsLeft + bold(ChatColor.RED) + " seconds remaining");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.7f, 0.9f);
                    });
                }
                secondsLeft--;
            }
        }.runTaskTimer(plugin, 0, 1L);
    }

    public void onEventTick() {

    }

}
