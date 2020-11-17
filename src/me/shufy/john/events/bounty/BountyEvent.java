package me.shufy.john.events.bounty;

import me.shufy.john.Main;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ThreadLocalRandom;

public class BountyEvent {

    public static final Main plugin = Main.getPlugin(Main.class);

    private World bountyEventWorld;
    private BukkitTask chanceRunnerTask;

    Player hunter, target;

    private static BountyEvent runningInstance;
    private static boolean instanceIsRunning;
    private static boolean isRunning;

    private double chance;
    private boolean ignoreChance = false;

    public BountyEvent(World bountyEventWorld, double chance) {
        this.bountyEventWorld = bountyEventWorld;
        this.chance = chance;
        this.chanceRunnerTask = chanceRunner().runTaskTimer(plugin, 20L, 20L * 15);
    }
    private BukkitRunnable chanceRunner() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!instanceIsRunning && !isRunning && bountyEventWorld.getPlayers().size() >= 2)
                if (ignoreChance || ThreadLocalRandom.current().nextDouble() < chance)
                    runBounty();
            }
        };
    }
    private void runBounty() {
        isRunning = true;
        instanceIsRunning = true;

    }
}
