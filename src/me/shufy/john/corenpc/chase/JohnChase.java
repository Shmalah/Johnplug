package me.shufy.john.corenpc.chase;

import me.shufy.john.Main;
import me.shufy.john.corenpc.JohnNpc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;
import java.util.logging.Level;

import static me.shufy.john.util.john.JohnUtility.*;


public class JohnChase {

    public static final Main plugin = Main.getPlugin(Main.class);

    public static BukkitTask chanceTask;
    public static BukkitTask johnChaseTask;
    public static BukkitTask johnNpcTask;

    public static double chance;
    public static int period;
    public static boolean running;
    public static boolean ignoringChance;
    public static JohnNpc johnNpc;
    public static World chaseWorld;

    public static void startAddon() {
        if (!chanceTask.isCancelled())
            throw new IllegalStateException("Tried to start John chase addon. John chase addon is already started.");
        Bukkit.getLogger().log(Level.INFO, "Starting john chase addon");
        chanceTask = chanceRunnable().runTaskTimer(plugin, 0, 20L * period);
    }

    public static BukkitRunnable chanceRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                chaseWorld = getWorldWithMostPlayers(); // update world every dice roll
                if (randomChanceNoDebug(chance) || ignoringChance)
                    if (!running)
                        startJohnChase();
                    else
                        Bukkit.getLogger().log(Level.SEVERE, "John chase task is already running! Why trying to start it again, bozo??");
            }
        };
    }

    public static BukkitRunnable johnChaseRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                spawnJohnInWorld(null);
            }
        };
    }

    public static void startJohnChase() {
        Bukkit.getLogger().log(Level.INFO, "Starting john chase!");
        if (Bukkit.getOnlinePlayers().isEmpty()) // if there's nobody online then what's the point of living.
            return;
        chanceTask.cancel(); // stop rolling dice
        johnChaseTask = JohnChase.johnChaseRunnable().runTask(plugin); // start john chase task
        running = true; // john chase is now running
    }

    public static void stopJohnChase() {
        Bukkit.getLogger().log(Level.INFO, "Stopping john chase!");
        johnChaseTask.cancel(); // stop john chase task
        running = false; // it's not running anymore
        chanceTask = chanceRunnable().runTaskTimer(plugin, 0, 20L * period); // start rolling dice
    }

    public static void chasePlayers(World world) {
        if (world == null) {
            if (chaseWorld != null)
                world = chaseWorld;
        } else if (johnNpc == null) {
            spawnJohnInWorld(null);
        }
        if (!johnNpcTask.isCancelled())
            johnNpcTask.cancel();
        johnNpcTask = new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskTimer(plugin, 3L, 1L);
    }

    public static void spawnJohnInWorld(World world) {
        if (world == null) {
            if (chaseWorld != null)
                world = chaseWorld;
            else
                throw new IllegalArgumentException("Chase world is null when provided world is null.. JohnChase");
        } else if (johnNpc != null) {
            johnNpc.destroy();
        }
        assert chaseWorld != null;
        if (world.getPlayers().isEmpty())
            throw new IllegalStateException("There are no players in the world \"" + world.getName() + "\" to spawn onto.. JohnChase");
        Location spawnLocation = Objects.requireNonNull(randomPlayer(chaseWorld)).getLocation();
        johnNpc = new JohnNpc(spawnLocation);
        johnNpc.spawn(chaseWorld.getPlayers());
    }
}
