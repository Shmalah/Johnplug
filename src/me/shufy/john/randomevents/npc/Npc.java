package me.shufy.john.randomevents.npc;

import com.mojang.authlib.GameProfile;
import me.shufy.john.Main;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R2.CraftServer;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.logging.Level;

import static me.shufy.john.util.JohnUtility.*;

public class Npc {

    public static List<Npc> allNpcs = new ArrayList<>();
    public static final Main plugin = Main.getPlugin(Main.class);

    private String displayName;
    private String uuid;
    private Location spawnLocation;
    private NpcFilter npcMode = NpcFilter.CLOSEST_PLAYER_OR_ENTITY; // default

    private EntityPlayer npcPlayer;
    private BukkitTask npcLoopTask;

    public boolean jumpscare = false;

    public Npc(String displayName, String uuid, Location spawnLocation) {
        this.displayName = displayName;
        this.uuid = uuid;
        this.spawnLocation = spawnLocation;

        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld)Bukkit.getWorld("world")).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.fromString(uuid), displayName);
        npcPlayer = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
        npcPlayer.setLocation(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), spawnLocation.getYaw(), spawnLocation.getPitch());
    }

    public void lookAt(Location point) {
        Vector vR = vectorFromLocToLoc(npcPlayer.getBukkitEntity().getEyeLocation(), point).normalize();
        float yaw = (float) Math.toDegrees(Math.atan2(vR.getZ(), vR.getX()))+90, pitch = (float) Math.toDegrees(Math.asin(vR.getY()))*-1;
        //Bukkit.getLogger().log(Level.INFO, "NPC " + this.hashCode() + " YAW/PITCH: " + yaw + " / " + pitch);
        sendNmsPackets(npcPlayer.getBukkitEntity().getWorld().getPlayers(), npcPlayer, new Object[] { yaw, pitch }, PacketType.NPC_ROTATION);
    }

    public void attack() {
        LivingEntity target = getClosestEntity(npcPlayer.getBukkitEntity().getLocation());
        if (target instanceof Player) {
            if(!((Player) target).getGameMode().equals(GameMode.SURVIVAL)) {
                return; // the target is not in survival mode, so john will not attempt to attack, as effort would be futile and thus a waste of precious john energy.
            }
        }
        double distanceToTarget = target.getLocation().toVector().subtract(npcPlayer.getBukkitEntity().getLocation().toVector()).length();
        if (distanceToTarget <= 3.0d) {
            try {
                npcPlayer.getBukkitEntity().attack(target);
            } catch (Exception ex) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to attack entity who is in range \"" + target.getName() + "\" : " + ex.getMessage());
            }
        } else {
            try {
                if (npcPlayer.getBukkitEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null)
                    target.damage(npcPlayer.getBukkitEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue(), npcPlayer.getBukkitEntity());
                else
                    target.damage(1.5d, npcPlayer.getBukkitEntity());
            } catch (Exception ex) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to attack entity out of range \"" + target.getName() + "\" : " + ex.getMessage());
            }
        }
    }

    public void moveStepped(Location location) {
        Vector vR = vectorFromLocToLoc(npcPlayer.getBukkitEntity().getLocation(), location).normalize();
        sendNmsPackets(npcPlayer.getBukkitEntity().getWorld().getPlayers(), npcPlayer, new Object[] { (double) vR.getX(), (double) vR.getY(), (double) vR.getZ() }, PacketType.NPC_MOVE);
    }

    public void moveTpStepped() {
        if (!getClosestPlayer(npcPlayer.getBukkitEntity()).getGameMode().equals(GameMode.SURVIVAL)) {
            return; // don't move towards players who are in creative for now..
        }
        Vector vR = vectorFromLocToLoc(npcPlayer.getBukkitEntity().getLocation(), getClosestPlayer(npcPlayer.getBukkitEntity()).getLocation()).normalize().multiply(0.2);
        npcPlayer.setLocation(npcPlayer.locX()+vR.getX(), npcPlayer.locY()+vR.getY(), npcPlayer.locZ()+vR.getZ(), npcPlayer.yaw, npcPlayer.pitch);
        sendNmsPackets(npcPlayer.getBukkitEntity().getWorld().getPlayers(), npcPlayer, new Object[] {  }, PacketType.NPC_TELEPORT);
    }

    public void summon() {
        this.npcLoopTask = npcLoop().runTaskTimer(plugin, 0, 1L);
        sendNmsPackets(spawnLocation.getWorld().getPlayers(), npcPlayer, new Object[]{}, PacketType.NPC_CREATION);
        allNpcs.add(this);
    }

    public void destroy() {
        if (npcLoopTask != null)
            if (!npcLoopTask.isCancelled())
                npcLoopTask.cancel();
        sendNmsPackets(new ArrayList<>(Bukkit.getOnlinePlayers()), npcPlayer, new Object[]{}, PacketType.NPC_DELETION);
        allNpcs.remove(this);
    }

    private BukkitRunnable npcLoop() {
        return new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                try {
                    lookAt(getClosestPlayer(npcPlayer.getBukkitEntity()).getEyeLocation());
                    if (jumpscare) {
                        moveStepped(getClosestPlayer(npcPlayer.getBukkitEntity()).getLocation());
                    } else {
                        moveTpStepped();
                        attack();
                    }
                } catch (Exception ex) {
                    if (ticks % 20 == 0) {
                        Bukkit.getLogger().log(Level.SEVERE, "NpcLoop caught an exception: " + ex.getMessage());
                    } else {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Bukkit.getLogger().log(Level.SEVERE, "NpcLoop caught an exception: " + ex.getMessage());
                            }
                        }.runTaskLater(plugin, 20-ticks);
                    }
                }
                ticks++;
            }
        };
    }


}
