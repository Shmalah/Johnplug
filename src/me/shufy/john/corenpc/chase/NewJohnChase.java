package me.shufy.john.corenpc.chase;

import me.shufy.john.Main;
import me.shufy.john.corenpc.JohnEntity;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.logging.Level;

import static me.shufy.john.util.john.JohnUtility.*;

public class NewJohnChase {
    public static final Main plugin = Main.getPlugin(Main.class);
    public static boolean ignoreChance;
    public static boolean isRunning;
    public NewJohnChase() {
        isRunning = false;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (randomChanceNoDebug(0.1d) || ignoreChance) {
                    if (!isRunning) {
                        chase();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 100L);
    }
    public void chase() {
        if (Bukkit.getOnlinePlayers().isEmpty())
            return;
        isRunning = true;
        int ticksToLive = randomInt(100, 300);
        EntityPlayer john = JohnEntity.createJohnEntity(randomLocationNearPlayer(Objects.requireNonNull(randomPlayer(getWorldWithMostPlayers())), 15));
        new BukkitRunnable() {
            private int ticksElapsed = 0;
            @Override
            public void run() {
                // attack if close enough
                if (JohnEntity.getJohnWorld(john).getPlayers().isEmpty()) {
                    Bukkit.getLogger().log(Level.SEVERE, "World players empty");
                    this.cancel();
                }
                try {
                    JohnEntity.attackEntity(john, Objects.requireNonNull(getClosestPlayer(JohnEntity.getJohnLocation(john))));
                    JohnEntity.followEntity(john, getClosestPlayer(JohnEntity.getJohnLocation(john)));
                } catch (NullPointerException ex) {
                    Bukkit.getLogger().log(Level.SEVERE, ex.getMessage());
                    this.cancel();
                }
                ticksElapsed++;
            }
        }.runTaskTimer(plugin, 1L, 1L);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isRunning)
                    endChase(john);
            }
        }.runTaskLater(plugin, ticksToLive);
    }
    public void endChase(EntityPlayer john) {
        JohnEntity.following = false;
        if (!JohnEntity.destroyJohnEntity(john, true))
            Bukkit.getLogger().log(Level.SEVERE, "Could not destroy john entity");
        isRunning = false;
    }
}
