package me.shufy.john.corenpc;

import me.shufy.john.Main;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.logging.Level;

import static me.shufy.john.util.john.JohnUtility.locationToString;

public class JohnGoal {
    public static final Main plugin = Main.getPlugin(Main.class);
    public EntityPlayer john;
    public boolean completed = false;
    public boolean look = true;
    public double goalForgiveness = 0.8d;
    public Location goal;
    public JohnGoal(EntityPlayer john, Location goal, boolean look) {
        this.john = john;
        this.look = look;
        this.goal = goal;
        JohnEntity.npcGoals.add(this);
        if (!run()) {
            Bukkit.getLogger().log(Level.WARNING, String.format("Could not run John Goal \"%s\" (%s) for John Entity \"%s\"", this.hashCode(), locationToString(goal), john.hashCode()));
            JohnEntity.npcGoals.remove(this);
        }
    }
    public JohnGoal(EntityPlayer john, Player goal) {
        this(john, goal.getLocation(), false);
    }
    private boolean run() {
        if (completed)
            return true;
        // prepare/analyze goal
        if (!JohnEntity.getJohnWorld(john).equals(goal.getWorld())) {
            Bukkit.getLogger().log(Level.WARNING, "John Entity world is not in the goal world.");
            return false;
        } else if (!JohnEntity.npcs.contains(john)) {
            Bukkit.getLogger().log(Level.WARNING, "John Entity does not exist, can't run goal on null John Entity");
            return false;
        } else if (Double.isNaN(JohnEntity.getJohnLocation(john).distance(goal)) || Double.isInfinite(JohnEntity.getJohnLocation(john).distance(goal))) {
            Bukkit.getLogger().log(Level.WARNING, "Distance from John Entity to goal location is infinite or NaN. Cannot traverse to goal.");
            return false;
        }

        // make john entity fulfill the goal
        Vector vGoal = goal.toVector().subtract(JohnEntity.getJohnLocation(john).toVector());
        Vector vGoalRel = vGoal.normalize().multiply(0.3d);
        new BukkitRunnable() {
            @Override
            public void run() {
                // debug particle
               // goal.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, goal, 1);

                // look
                if (look) {
                    Vector vLook = goal.toVector().subtract(JohnEntity.getJohnEyeLocation(john).toVector()).normalize();
                    JohnEntity.setLookingDirection(john, vLook);
                }

                // step
                john.setPosition(john.locX()+vGoalRel.getX(), john.locY()+vGoalRel.getY(), john.locZ()+vGoalRel.getZ());
                JohnEntity.sendPacket(new PacketPlayOutEntityTeleport(john),
                        JohnEntity.getJohnWorld(john).getPlayers());

                // evaluate
                if (JohnEntity.getJohnLocation(john).distance(goal) <= goalForgiveness)
                    completed = true;
                if (completed) {
                    JohnEntity.debugLog(Level.INFO, "John " + john.hashCode() + " met its goal " + locationToString(goal));
                    JohnEntity.npcGoals.remove(JohnGoal.this);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1L);
        return true;
    }
}
