package me.shufy.john.randomevents.challenges;

import me.shufy.john.Main;
import me.shufy.john.randomevents.npc.JohnChallenge;
import org.bukkit.scheduler.BukkitRunnable;

public class Challenge implements JohnChallenge {

    public static final Main plugin = Main.getPlugin(Main.class);
    static boolean challengeAlreadyRunning = false;

    public Challenge() {
        new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskTimer(plugin, 0, 20L);
    }

}
