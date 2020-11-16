package me.shufy.john.corenpc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.shufy.john.Main;
import me.shufy.john.scare.Spooker;
import me.shufy.john.util.JohnUtility;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;

public class JohnNpc {

    /*
    * INFO ABOUT JOHN NPC
    * - The john npc can't teleport or jump across worlds. The instance must be destroyed and then renewed with the spawn location in the target world in order for john to be in a different world.
    */

    EntityPlayer npc;
    Location spawnLocation;
    boolean spawnedIn = false;

    public static Collection<JohnNpc> allNpcs = new ArrayList<>();

    // creates the npc but DOES NOT appear in the world to anyone. You're just setting the spawn location for where IT SHOULD APPEAR when it DOES appear to other players.
    public JohnNpc(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) spawnLocation.getWorld()).getHandle(); // Change "world" to the world the NPC should be spawned in.
        GameProfile skin169665813 = new GameProfile(UUID.fromString("a6bc2231-fe68-4b75-b796-b074b763410b"), "John");
        skin169665813.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYwNTEzMzUzMzE4OSwKICAicHJvZmlsZUlkIiA6ICI4MmM2MDZjNWM2NTI0Yjc5OGI5MWExMmQzYTYxNjk3NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3ROb3RvcmlvdXNOZW1vIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzU3ZjMyYjEwOGMxNjZhNWNkNmI4YWQzOTA3ZTg3MWMyYjYyMTRmY2VhMDk2YzZkMTY5YjUyNDhhNDg3Y2NiYjQiCiAgICB9CiAgfQp9", "ALNkre7PT/ufdze5/fFA9cBDwTX/xtO7l38rn/VxjjW2qd0pQiBw09fYYmmlhUWRf3ohr7JbrfLho4fu09WpZGn4+mgHVg2VqOalqFySiG8MX4WcI6whYfjcn6ijLAmfTcaDY9pph+JQHwzi8V1ByvcqLQldzBt/SD9oxvc1PqN3KCPstzfIYEJGeLis7M6gc+qaI7xCu/gKOiNcyGLvKUu5InDgPgtw/7DBPs6a1gaY6yutG7yJDsQqBaFyERbsOdwNytsiRehvJ/JaWRznO0XDrXYA+GiNKaRP6ehpiHuRIFENEIIMBRncjjL+Xj3/TS3oANjPBHCmeq0q8Z05v0N5vPvO8vyJWYdw5YLPfubePGsf2n/KiGYroe3n7aohViwzBIzGUVNTxcHchMUI7X/RtMSm3y8iuo5ByLcRvxKU6Jn/vnI3qCYGLqIf/+TnzwseUl5FvMMZfezVOCEU5tzZwBriDIn7x5dcBTu1Z2qVXe4hIatpkUCPlq+kREkaJu7VCs7AiP4hWFvdFfyj+TkFvunzNkUd5Tf3SRQYxlJaD2niHfAzlH/sR6u3N5WZLeGGe6R+LmglSHmT8vIhw/YbvwlQyht/rDmiyA7jT+BekEB8bjFRLx4gd+2N6qLJBZhB8UbeAhyJppG0nVafbKGjw14YU8nW4EbnQOfaojw="));
        npc = new EntityPlayer(nmsServer, nmsWorld, skin169665813, new PlayerInteractManager(nmsWorld)); // This will be the EntityPlayer (NPC) we send with the sendNPCPacket method.
        npc.setLocation(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), spawnLocation.getYaw(), spawnLocation.getPitch());
    }

    /**
     * Officially spawns the npc into the world
     * @param playersToAppearTo
     * <p>The players that john should appear to. Must be a list with at least 1 player</p>
     */
    public void spawn(Collection<Player> playersToAppearTo) {
        // for exception/debug purposes
        if (isSpawnedIn()) {
            Bukkit.getLogger().log(Level.WARNING, String.format("Tried to spawn in John NPC \"%s\" in the world \"%s\" when the John NPC \"%s\" is already spawned in.", this.hashCode(), spawnLocation.getWorld().getName(), this.hashCode()));
            return;
        }
        Player curplayer = null;
        try {
            for (Player player : playersToAppearTo) {
                curplayer = player;
                PlayerConnection conn = ((CraftPlayer)player).getHandle().playerConnection;
                conn.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
                conn.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
                conn.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
            }
            spawnedIn = true;
            allNpcs.add(this);
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, String.format("Tried to send packet to player \"%s\" in world \"%s\" when an exception occurred: %s", curplayer.getName(), spawnLocation.getWorld().getName(), ex.getMessage()));
        }
    }

    public void look(Location point) {
        Vector vLook = point.toVector().subtract(npc.getBukkitEntity().getEyeLocation().toVector()).normalize();
        npc.yaw = (float) Math.toDegrees(Math.atan2(vLook.getZ(), vLook.getX()))-90; // -90 angle correction
        npc.pitch = (float) Math.toDegrees(Math.asin(vLook.getY()))*-1; // negate angle to correct pitch
        Player curplayer = null;
        try {
            for (Player player : spawnLocation.getWorld().getPlayers()) {
                curplayer = player;
                PlayerConnection conn = ((CraftPlayer)player).getHandle().playerConnection;
                conn.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte)(npc.yaw * 256 / 360))); // yaw packet
                conn.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte)(npc.yaw * 256 / 360), (byte)(npc.pitch * 256 / 360), true)); // pitch packet
            }
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, String.format("Tried to send a look packet to player \"%s\" for John NPC \"%s\" but an exception was thrown: %s", curplayer.getName(), this.hashCode(), ex.getMessage()));
        }
    }

    public void move(double x, double y, double z) {
        npc.setLocation(npc.locX()+x, npc.locY()+y, npc.locZ()+z, npc.yaw, npc.pitch);
        Player curplayer = null;
        try {
            for (Player player : spawnLocation.getWorld().getPlayers()) {
                curplayer = player;
                PlayerConnection conn = ((CraftPlayer)player).getHandle().playerConnection;
                conn.sendPacket(new PacketPlayOutEntityTeleport(npc));
            }
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, String.format("Tried to send a move packet to player \"%s\" for John NPC \"%s\" but an exception was thrown: %s", curplayer.getName(), this.hashCode(), ex.getMessage()));
        }
    }

    public void takeKnockback(Location source, double amount) {
        Vector vKb = npc.getBukkitEntity().getLocation().toVector().subtract(source.toVector()).multiply(amount);
        new BukkitRunnable() {
            @Override
            public void run() {
                // knocks back the npc until the vector is effectively 0
                if (vKb.length() < 0.05)
                    this.cancel();
                move(vKb.getX(), vKb.getY(), vKb.getZ());
                vKb.multiply(0.5); // halve the vector
            }
        }.runTaskTimer(me.shufy.john.Main.getPlugin(Main.class), 0, 1L);
    }

    // overload for player source
    public void takeKnockback(Player player, double amount) {
        Vector vKb = player.getLocation().getDirection().multiply(amount);
        new BukkitRunnable() {
            @Override
            public void run() {
                // knocks back the npc until the vector is effectively 0
                if (vKb.length() < 0.05)
                    this.cancel();
                move(vKb.getX(), vKb.getY(), vKb.getZ());
                vKb.multiply(0.5); // halve the vector
            }
        }.runTaskTimer(me.shufy.john.Main.getPlugin(Main.class), 0, 1L);
    }

    public boolean isAutoTargeting = false;
    public BukkitTask autoTargetTask;

    public void autoTarget() {
        if (isAutoTargeting)
            return;
        autoTargetTask = new BukkitRunnable() {
            private Player target = null;
            private int noAttackTicks = 6;
            private int ticks = 0;
            @Override
            public void run() {
                // select target
                if (!Bukkit.getOnlinePlayers().isEmpty()) {
                    if (!spawnLocation.getWorld().getPlayers().isEmpty()) {
                        if (target == null || JohnUtility.getClosestPlayer(getNpc().getBukkitEntity()) != target) {
                            target = JohnUtility.getClosestPlayer(getNpc().getBukkitEntity());
                            follow(target); // only follow if it's a new player
                        }

                    }
                }
                // go after them if they exist
                if (target != null) {
                    // attack if it can
                    if (canAttack()) attack(target);
                }
                ticks++;
            }
            private boolean canAttack() {
                return ticks % noAttackTicks == 0;
            }
        }.runTaskTimer(Spooker.plugin, 0, 1L);
    }

    public void stopAutoTarget() {
        if (!isAutoTargeting)
            return;
        autoTargetTask.cancel();
        isAutoTargeting = false;
    }

    public void follow (Player player) {
        if (player == null)
            player = JohnUtility.getClosestPlayer(npc.getBukkitEntity());
        Player finalPlayer = player;
        new BukkitRunnable() {
            @Override
            public void run() {
                Vector vDelta = finalPlayer.getLocation().toVector().subtract(getNpc().getBukkitEntity().getLocation().toVector()).normalize().multiply(0.3d);
                look(finalPlayer.getEyeLocation()); // look into the eyes of your victim.
                if (getNpc().getBukkitEntity().getLocation().getBlock().isLiquid())
                    vDelta.multiply(0.5);
                move(vDelta.getX(), vDelta.getY(), vDelta.getZ()); // move toward your victim, john. It's time.
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 20L, 1L);
    }

    public void attack(Player player) {
        if (player.getLocation().distance(getNpc().getBukkitEntity().getLocation()) <= 3)
            player.damage(2.5d, getNpc().getBukkitEntity());
    }

    public void destroy() {
        if (!isSpawnedIn()) {
            Bukkit.getLogger().log(Level.WARNING, String.format("Tried to destroy the John NPC \"%s\" when the John NPC \"%s\" is already destroyed.", this.hashCode(), this.hashCode()));
            return;
        }
        Player curplayer = null;
        try {
            for (Player player : spawnLocation.getWorld().getPlayers()) {
                curplayer = player;
                PlayerConnection conn = ((CraftPlayer)player).getHandle().playerConnection;
                conn.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                conn.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
            }
            allNpcs.remove(this);
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, String.format("Tried to destroy the john npc \"%s\" in world \"%s\" for player \"%s\" when an exception occurred: %s", this.hashCode(), spawnLocation.getWorld().getName(), curplayer.getName(), ex.getMessage()));
        }
    }

    // only use with ondisable/when the server is reloading or closing or etc.
    public static void deinitializeJohns() {
        if (allNpcs.isEmpty())
            return;
        JohnNpc curNpc = null;
        try {
            for (JohnNpc johnNpc : allNpcs) {
                if (johnNpc == null || johnNpc.getNpc() == null)
                    continue;
                curNpc = johnNpc;
                johnNpc.destroy();
                Bukkit.getLogger().log(Level.INFO, String.format("De-initialized the NPC \"%s\"", curNpc.hashCode()));
            }
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, String.format("An exception occurred de-initializing the npc \"%s\": %s", curNpc.hashCode(), ex.getMessage()));
        }
    }

    public EntityPlayer getNpc() {
        return npc;
    }

    public boolean isSpawnedIn() {
        return spawnedIn;
    }

    public static Collection<JohnNpc> getAllNpcs() {
        return allNpcs;
    }
}
