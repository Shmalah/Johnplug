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

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import static me.shufy.john.util.JohnUtility.bold;
import static me.shufy.john.util.JohnUtility.vectorFromLocToLoc;

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
                  Bukkit.getLogger().log(Level.INFO, "Bounty event not running because john event instance running = " + JohnEvent.isEventInstanceRunning() + ", player size good = " + (bountyEventWorld.getPlayers().size() >= 2) + " Bounty instances running = " + instanceRunning);
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
            }
        }.runTaskTimer(plugin, 0, 2L);

        new BukkitRunnable() {
            @Override
            public void run() {
                // END OF BOUNTY
                if (!hasKilled) {
                    JohnNpc john = new JohnNpc(JohnUtility.randomLocationNearPlayer(hunter, 5));
                    Bukkit.broadcastMessage(bold(ChatColor.GOLD) + hunter.getName() + bold(ChatColor.RED) + " failed to fulfill their bounty on " + bold(ChatColor.AQUA) + target.getName());
                    JohnUtility.broadcastSound(Sound.ENTITY_ENDERMAN_STARE, new SoundInfo(1f, ThreadLocalRandom.current().nextFloat()));
                    JohnUtility.broadcastSound(Sound.ENTITY_ENDER_DRAGON_GROWL, new SoundInfo(1f, ThreadLocalRandom.current().nextFloat()));

                    BukkitTask johnTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            // CONTROL JOHN
                            john.move(vectorFromLocToLoc(john.npcLoc(), hunter.getLocation()).normalize().multiply(0.4d));
                            john.look(hunter.getEyeLocation());
                            if (john.npcLoc().distance(hunter.getLocation()) <= 3)
                                john.attack(hunter);
                        }
                    }.runTaskTimer(plugin, 3L, 1L);

                    hunter.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*15, 2));
                    hunter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*15, 1));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            // cleanup john npc 15 seconds after he spawns
                            johnTask.cancel();
                            john.destroy();
                            for (PotionEffect activePotionEffect : hunter.getActivePotionEffects()) {
                                hunter.removePotionEffect(activePotionEffect.getType());
                            }
                            stopBounty();
                        }
                    }.runTaskLater(plugin, 20L * 15);
                }
            }
        }.runTaskLater(plugin, 20L * 120);
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
