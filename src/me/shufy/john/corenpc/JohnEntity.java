package me.shufy.john.corenpc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.shufy.john.Main;
import me.shufy.john.util.john.JohnUtility;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static me.shufy.john.util.john.JohnUtility.johnBan;
import static me.shufy.john.util.john.JohnUtility.vectorFromLocToLoc;

public class JohnEntity implements Listener {

    public static final me.shufy.john.Main plugin = me.shufy.john.Main.getPlugin(Main.class);
    public static List<EntityPlayer> npcs = new ArrayList<>();
    public static List<JohnGoal> npcGoals = new ArrayList<>();
    public static HashMap<Player, Integer> combatLog = new HashMap<>();
    public static BukkitTask combatLogTask;
    public static boolean debug = true;

    // TODO make john fishing rod do knockback on this new JohnEntity class

    /* ********************** */
    /*  STATIC JOHN METHODS
    /* *********************** */

    public static EntityPlayer createJohnEntity(Location location) {
        EntityPlayer john;
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle(); // Change "world" to the world the NPC should be spawned in.
        GameProfile skin169665813 = new GameProfile(UUID.fromString("a6bc2231-fe68-4b75-b796-b074b763410b"), "John");
        skin169665813.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYwNTEzMzUzMzE4OSwKICAicHJvZmlsZUlkIiA6ICI4MmM2MDZjNWM2NTI0Yjc5OGI5MWExMmQzYTYxNjk3NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3ROb3RvcmlvdXNOZW1vIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzU3ZjMyYjEwOGMxNjZhNWNkNmI4YWQzOTA3ZTg3MWMyYjYyMTRmY2VhMDk2YzZkMTY5YjUyNDhhNDg3Y2NiYjQiCiAgICB9CiAgfQp9", "ALNkre7PT/ufdze5/fFA9cBDwTX/xtO7l38rn/VxjjW2qd0pQiBw09fYYmmlhUWRf3ohr7JbrfLho4fu09WpZGn4+mgHVg2VqOalqFySiG8MX4WcI6whYfjcn6ijLAmfTcaDY9pph+JQHwzi8V1ByvcqLQldzBt/SD9oxvc1PqN3KCPstzfIYEJGeLis7M6gc+qaI7xCu/gKOiNcyGLvKUu5InDgPgtw/7DBPs6a1gaY6yutG7yJDsQqBaFyERbsOdwNytsiRehvJ/JaWRznO0XDrXYA+GiNKaRP6ehpiHuRIFENEIIMBRncjjL+Xj3/TS3oANjPBHCmeq0q8Z05v0N5vPvO8vyJWYdw5YLPfubePGsf2n/KiGYroe3n7aohViwzBIzGUVNTxcHchMUI7X/RtMSm3y8iuo5ByLcRvxKU6Jn/vnI3qCYGLqIf/+TnzwseUl5FvMMZfezVOCEU5tzZwBriDIn7x5dcBTu1Z2qVXe4hIatpkUCPlq+kREkaJu7VCs7AiP4hWFvdFfyj+TkFvunzNkUd5Tf3SRQYxlJaD2niHfAzlH/sR6u3N5WZLeGGe6R+LmglSHmT8vIhw/YbvwlQyht/rDmiyA7jT+BekEB8bjFRLx4gd+2N6qLJBZhB8UbeAhyJppG0nVafbKGjw14YU8nW4EbnQOfaojw="));
        john = new EntityPlayer(nmsServer, nmsWorld, skin169665813, new PlayerInteractManager(nmsWorld)); // This will be the EntityPlayer (NPC) we send with the sendNPCPacket method.
        john.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, john), location.getWorld().getPlayers());
        sendPacket(new PacketPlayOutNamedEntitySpawn(john), location.getWorld().getPlayers());
        sendPacket(new PacketPlayOutEntityHeadRotation(john, (byte) (john.yaw * 256 / 360)), location.getWorld().getPlayers());

        npcs.add(john);

        debugLog(Level.INFO, String.format("Summoned John Entity %s", john.hashCode()));
        return john;
    }

    public static boolean destroyJohnEntity(EntityPlayer john, boolean removeFromList) {
        try {
            sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, john), new ArrayList<>(Bukkit.getOnlinePlayers()));
            sendPacket(new PacketPlayOutEntityDestroy(john.getId()), new ArrayList<>(Bukkit.getOnlinePlayers()));
            if (removeFromList)
                npcs.remove(john);
            debugLog(Level.INFO, String.format("Destroyed John Entity %s", john.hashCode()));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public static World getJohnWorld(EntityPlayer john) {
        return john.getBukkitEntity().getWorld();
    }
    public static Location getJohnLocation(EntityPlayer john) {
        return john.getBukkitEntity().getLocation();
    }
    public static Location getJohnEyeLocation(EntityPlayer john) {
        return john.getBukkitEntity().getEyeLocation();
    }
    public static double getJohnDistanceToPlayer(EntityPlayer john, Player player) {
        if (!getJohnWorld(john).equals(player.getWorld()))
            return Double.NaN;
        return player.getLocation().toVector().subtract(getJohnLocation(john).toVector()).length();
    }
    public static Player getClosestPlayer(EntityPlayer john) {
        try {
            if (getJohnWorld(john).getPlayers().isEmpty())
                return null;
            Player closest = null;
            for (Player player : getJohnWorld(john).getPlayers()) {
                if (closest == null || getJohnDistanceToPlayer(john, player) < getJohnDistanceToPlayer(john, closest))
                    closest = player;
            }
            return closest;
        } catch (NullPointerException ex) {
            Bukkit.getLogger().log(Level.WARNING, "There are no players in world \"" + getJohnWorld(john).getName() + "\" when trying to get closest player to John Entity \"" + john.hashCode() + "\"");
            Bukkit.getLogger().log(Level.WARNING, ex.getMessage());
            return john.getBukkitEntity(); // for safety
        }
    }
    public static Collection<JohnGoal> getJohnGoals(EntityPlayer john) {
        return npcGoals.stream().filter(goal -> goal.john.equals(john)).collect(Collectors.toList());
    }
    public static JohnGoal addJohnGoal(EntityPlayer john, Location location) {
        return new JohnGoal(john, location, true);
    }
    public static JohnGoal addJohnGoal(EntityPlayer john, Player player) {
        return addJohnGoal(john, player.getLocation());
    }
    public static void addContinuousJohnGoal(EntityPlayer john, LivingEntity entity) {
        new BukkitRunnable() {
            JohnGoal goal = new JohnGoal(john, entity.getLocation(), false);
            @Override
            public void run() {
                JohnEntity.setLookingDirection(john, vectorFromLocToLoc(JohnEntity.getJohnEyeLocation(john), entity.getEyeLocation()));
                if (goal.completed || entity.getLocation() != goal.goal) {
                    getJohnGoals(john).forEach(goal -> goal.completed = true); // cancel any current goals
                    debugLog(Level.INFO, "Goal " + goal.hashCode() + " for John Entity " + john.hashCode() + " has been completed. Reassigning (continuous)");
                    goal = new JohnGoal(john, entity.getLocation(), false); // start a new goal toward entity
                }
            }
        }.runTaskTimer(plugin, 0, 6L);
    }
    public static void attackEntity(EntityPlayer john, LivingEntity entity) {
        if (!npcs.contains(john))
            return;
        if (vectorFromLocToLoc(JohnEntity.getJohnLocation(john), entity.getLocation()).length() <= 3)
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.getGameMode().equals(GameMode.CREATIVE))
                return;
            sendPacket(new PacketPlayOutAnimation(john, 0), JohnEntity.getJohnWorld(john).getPlayers()); // swing left arm
            player.damage(2.5, john.getBukkitEntity());
            combatLog.put(player, 10);
            try { combatLogTask.cancel(); } catch (Exception ex) { /* nothing */ }
            combatLogTask = new BukkitRunnable() {
                @Override
                public void run() {
                    if (combatLog.get(player) > 0) {
                        combatLog.put(player, combatLog.get(player)-1);
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 20L);
        }
    }
    public static boolean following = false;
    public static void followEntity(EntityPlayer john, LivingEntity entity) {
        if (following || !npcs.contains(john))
            return;
        following = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!entity.isDead()) {

                    Vector vDir = JohnUtility.vectorFromLocToLoc(JohnEntity.getJohnLocation(john), entity.getLocation());
                    Vector vLook = JohnUtility.vectorFromLocToLoc(JohnEntity.getJohnEyeLocation(john), entity.getEyeLocation()).normalize();
                    Vector vMove = vDir.normalize().multiply(0.3d);

                    JohnEntity.setLookingDirection(john, vLook);
                    john.setPosition(john.locX()+vMove.getX(), john.locY()+vMove.getY(), john.locZ()+vMove.getZ());
                    JohnEntity.sendPacket(new PacketPlayOutEntityTeleport(john), JohnEntity.getJohnWorld(john).getPlayers());

                }
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (!player.isOnline()) {
                        if (combatLog.get(player) != 0)
                            johnBan(player, 20);
                        following = false;
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1L);
    }
    public static void setLookingDirection(EntityPlayer john, Vector vDirection) {
        float yaw = (float) (Math.toDegrees(Math.atan2(vDirection.getZ(), vDirection.getX())))-90;
        float pitch = (float) (Math.toDegrees(Math.asin(vDirection.getY())))*-1;
        john.pitch = pitch;
        john.yaw = yaw;
        sendPacket(new PacketPlayOutEntityHeadRotation(john, (byte) (yaw * 256 / 360)), getJohnLocation(john).getWorld().getPlayers());
        sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(john.getId(), (byte)(yaw * 256 / 360), (byte)(pitch * 256 / 360), true), getJohnLocation(john).getWorld().getPlayers());
    }
    public static void destroyAll() {
        npcs.forEach(john -> destroyJohnEntity(john, false));
        npcs.clear();
    }
    protected static void debugLog(Level lvl, String message) {
        if (debug) Bukkit.getLogger().log(lvl, message);
    }
    protected static void sendPacket(Packet<?> packet, Collection<Player> players) {
        players.forEach(player -> ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet));
    }
    protected static void sendPacket(Packet<?> packet, Player player) {
        sendPacket(packet, Collections.singleton(player));
    }

    /* ********************** */
    /*  EVENT LISTENERS
    /* *********************** */

    @EventHandler
    public void playerJoin (PlayerJoinEvent e) {
        combatLog.putIfAbsent(e.getPlayer(), 0);
        for (EntityPlayer john : npcs) {
            sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, john), e.getPlayer());
            sendPacket(new PacketPlayOutNamedEntitySpawn(john), e.getPlayer());
            sendPacket(new PacketPlayOutEntityHeadRotation(john, (byte) (john.yaw * 256 / 360)), e.getPlayer());
        }
    }

    @EventHandler
    public void playerLeave (PlayerQuitEvent e) {
        if (combatLog.get(e.getPlayer()) != 0)
            johnBan(e.getPlayer(), 30);
    }
}
