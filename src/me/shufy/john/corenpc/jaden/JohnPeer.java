package me.shufy.john.corenpc.jaden;

import me.shufy.john.Main;
import me.shufy.john.corenpc.JohnNpc;
import me.shufy.john.util.john.JohnUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import static me.shufy.john.util.john.JohnUtility.*;

public class JohnPeer {

    public static final Main plugin = Main.getPlugin(Main.class);

    public long period = (20L * 30);
    public double chance = 0.15d;

    public boolean ignoringChance;
    public static boolean isPeering;

    public BukkitTask peerRunnerTask;
    public BukkitTask peerTask;

    public JohnNpc john;

    public JohnPeer() {
        peerRunner();
    }

    private void peerRunner() {
        this.peerRunnerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (randomChanceNoDebug(chance) || ignoringChance) {
                    if (!isPeering)
                        peer();
                }
            }
        }.runTaskTimer(plugin, 0, period);
    }

    public void peer() {
        isPeering = true;
        peerRunnerTask.cancel();
        int appearanceTime = randomInt(5, 10);
        this.peerTask = peerRunnable().runTask(plugin);
        new BukkitRunnable() {
            @Override
            public void run() {
                JohnPeer.this.stopPeer();
            }
        }.runTaskLater(plugin, (20L * appearanceTime));
    }


    // TODO fix peer
    private BukkitRunnable peerRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().isEmpty()) {
                    stopPeer();
                } else {
                    World peerWorld = getWorldWithMostPlayers(); // can't be empty if there are players online
                    Player peerVictim = randomPlayer(peerWorld);
                    assert peerVictim != null;

                    Block peerBlock = JohnUtility.findBlockWithMaterialContainsInLOS(peerVictim, "LOG");

                    if (peerBlock == null) {
                        // can't peer
                        john = new JohnNpc(peerVictim.getLocation());
                        Bukkit.getLogger().log(Level.INFO, "Peer block is null");
                        return;
                    }

                    peerVictim.sendMessage("Block found: " + locationToString(peerBlock.getLocation()) + " | " + peerBlock.getType().name());
                    Location peerLocation = peerBlock.getLocation();
                    Bukkit.getLogger().log(Level.INFO, "Making john peer at location " + locationToString(peerLocation));
                    john = new JohnNpc(peerLocation);
                    john.spawn(Collections.singleton(peerVictim));
                    john.swingArm();
                    peerVictim.playSound(john.npcLoc(), randomSoundWhoContains("AMBIENT"), 1f, ThreadLocalRandom.current().nextFloat());
                }
            }
        };
    }

    public void stopPeer() {
        JohnPeer.this.peerTask.cancel();
        JohnPeer.this.john.destroy();
        isPeering = false;
        peerRunner();
    }
}
