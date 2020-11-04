package me.shufy.john.randomevents.challenges;

import me.shufy.john.Main;
import me.shufy.john.randomevents.npc.JohnChallenge;
import org.bukkit.scheduler.BukkitRunnable;

import static me.shufy.john.util.JohnUtility.randomChance;

public class Challenge implements JohnChallenge {

    public static final Main plugin = Main.getPlugin(Main.class);
    static boolean challengeAlreadyRunning = false;
    int eventDuration = 60; // default

    public Challenge() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (randomChance(0.10d)) {
                    if (!challengeAlreadyRunning) {
                        challengeAlreadyRunning = true;
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
        }.runTaskTimer(plugin, 0, 1L);
    }

    public void onEventTick() {

    }

}
