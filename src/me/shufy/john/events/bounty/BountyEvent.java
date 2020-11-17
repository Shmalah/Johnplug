package me.shufy.john.events.bounty;

import me.shufy.john.Main;
import me.shufy.john.corenpc.JohnNpc;
import me.shufy.john.events.JohnEvent;
import me.shufy.john.util.JohnUtility;
import me.shufy.john.util.PlayerPair;
import me.shufy.john.util.SoundInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import static me.shufy.john.util.JohnUtility.bold;

public class BountyEvent {

    public static final Main plugin = Main.getPlugin(Main.class);

    private World bountyEventWorld;
    private BukkitTask chanceRunnerTask;

    Player hunter, target;

    private static BountyEvent runningInstance;
    private static boolean instanceRunning;
    private static boolean running;

    private double chance;
    private boolean ignoreChance = false;

    public BountyEvent(World bountyEventWorld, double chance) {
        this.bountyEventWorld = bountyEventWorld;
        this.chance = chance;
        this.chanceRunnerTask = chanceRunner().runTaskTimer(plugin, 20L, 20L * 15);
    }

    private BukkitRunnable chanceRunner() {
        Bukkit.getLogger().log(Level.INFO, "Activated the bounty event");
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!instanceRunning && !running && bountyEventWorld.getPlayers().size() >= 2 && !JohnEvent.isEventInstanceRunning()) {
                    if (ignoreChance || ThreadLocalRandom.current().nextDouble() < chance)
                        runBounty();
                } else {
                //  Bukkit.getLogger().log(Level.INFO, "Bounty event not running because john event instance running = " + JohnEvent.isEventInstanceRunning() + ", player size good = " + (bountyEventWorld.getPlayers().size() >= 2) + " Bounty instances running = " + instanceRunning);
                }
            }
        };
    }

    // protected so bounty event listener can access
    protected boolean hasKilled = false;

    private void runBounty() {
        running = true;
        instanceRunning = true;
        runningInstance = this;

        PlayerPair hunterTargetPair = new PlayerPair(bountyEventWorld);
        this.hunter = hunterTargetPair.first;
        this.target = hunterTargetPair.second;

        this.hunter.sendTitle(bold(ChatColor.DARK_RED) + "KILL " + bold(ChatColor.GOLD) + this.target.getName().toUpperCase(), "You have 2 minutes", 60, 120, 60);
        this.hunter.playSound(this.hunter.getLocation(), Sound.AMBIENT_CAVE, 0.6f, 1f);

        hasKilled = false;

        new BukkitRunnable() {
            private int ticks = 0;
            private int seconds = 0;
            @Override
            public void run() {
                // check every tick or so if bounty is fulfilled
                if (hasKilled) {
                    // bounty was successfully fulfilled
                    hunter.sendMessage(bold(ChatColor.GOLD) + "You've successfully fulfilled the bounty placed on " + bold(ChatColor.RED) + target.getName());
                    hunter.playSound(hunter.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                    stopBounty();
                    this.cancel();
                }
                ticks++;
                if (ticks % 20 == 0)
                    seconds++;
                if (((seconds % 15 == 0 && seconds != 120) || seconds >= 110) && ticks % 20 == 0) {
                    // announce time left
                    hunter.sendMessage(bold(ChatColor.RED) + "You only have " + bold(ChatColor.AQUA) + (120-seconds) + bold(ChatColor.RED) + " seconds left to annihilate " + bold(ChatColor.GOLD) + target.getName());
                    hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.9f, 0.9f);
                } else if (seconds == 120) {
                    // END OF BOUNTY
                    if (!hasKilled) {
                        JohnNpc john = new JohnNpc(JohnUtility.randomLocationNearPlayer(hunter, 5));
                        Bukkit.broadcastMessage(bold(ChatColor.GOLD) + hunter.getName() + bold(ChatColor.RED) + " failed to fulfill their bounty on " + bold(ChatColor.AQUA) + target.getName());
                        JohnUtility.broadcastSound(Sound.ENTITY_ENDERMAN_STARE, new SoundInfo(1f, ThreadLocalRandom.current().nextFloat()));
                        JohnUtility.broadcastSound(Sound.ENTITY_ENDER_DRAGON_GROWL, new SoundInfo(1f, ThreadLocalRandom.current().nextFloat()));

                        john.spawn(Collections.singleton(hunter));

                        john.targetPlayer(hunter);

                        hunter.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*15, 2));
                        hunter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*15, 1));

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                // cleanup john npc 15 seconds after he spawns
                                john.destroy();
                                for (PotionEffect activePotionEffect : hunter.getActivePotionEffects()) {
                                    hunter.removePotionEffect(activePotionEffect.getType());
                                }
                                stopBounty();
                            }
                        }.runTaskLater(plugin, 20L * 15);
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1L);
    }

    private void stopBounty() {
        running = false;
        instanceRunning = false;
        runningInstance = null;
    }

    public static BountyEvent getRunningInstance() {
        return runningInstance;
    }
    public double getChance() {
        return chance;
    }

    public boolean isIgnoreChance() {
        return ignoreChance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public void setIgnoreChance(boolean ignoreChance) {
        this.ignoreChance = ignoreChance;
    }

    public World getBountyEventWorld() {
        return bountyEventWorld;
    }

    public static boolean isInstanceRunning() {
        return instanceRunning;
    }

    public static boolean isRunning() {
        return running;
    }

    public Player getHunter() {
        return hunter;
    }

    public Player getTarget() {
        return target;
    }
}
