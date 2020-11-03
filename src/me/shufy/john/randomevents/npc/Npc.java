package me.shufy.john.randomevents.npc;

import com.mojang.authlib.GameProfile;
import me.shufy.john.Main;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R2.CraftServer;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.logging.Level;

import static me.shufy.john.util.JohnUtility.*;

public class Npc {

    static List<Npc> allNpcs = new ArrayList<>();
    public static final Main plugin = Main.getPlugin(Main.class);
    boolean spawnedIn = false;
    MinecraftServer minecraftServer;
    WorldServer worldServer;
    GameProfile gameProfile;
    EntityPlayer npcPlayer;

    private HashMap<NpcAbility, Boolean> abilityBooleanHashMap = new HashMap<>();
    private NpcFilter npcFilterMode = NpcFilter.CLOSEST_PLAYER_OR_ENTITY; // default npc filter/npc mode

    public Npc(String name, UUID skin) {
        minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        gameProfile = new GameProfile(skin, name);
        for (NpcAbility value : NpcAbility.values()) {
            abilityBooleanHashMap.putIfAbsent(value, false);
        }
        npcLogicLoop().runTaskTimer(plugin, 0, 1L);
    }

    public Npc(String name, String uuidSkin) {
        this (name, UUID.fromString(uuidSkin));
    }

    public void summon(Location location) {
        if (!isSpawnedIn()) {
            worldServer = ((CraftWorld)location.getWorld()).getHandle();
            npcPlayer = new EntityPlayer(minecraftServer, worldServer, gameProfile, new PlayerInteractManager(worldServer));
            npcPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            sendPacket(PacketType.NPC_CREATION, location.getWorld().getPlayers());
            spawnedIn = true;
        } else {
            throw new IllegalStateException("Can't summon an already existing NPC. Did you mean to use teleport() or move() or somethn?");
        }
    }

    public void destroy() {
        if (isSpawnedIn()) {
            sendPacket(PacketType.NPC_DELETION, new ArrayList<>(Bukkit.getOnlinePlayers()));
            spawnedIn = false;
        } else {
            throw new IllegalStateException("Can't destroy the Npc " + this.hashCode() + " when " + this.hashCode() + " is not spawned in/is already destroyed.");
        }
    }

    public void moveTo(Location location) {
        if (!npcPlayer.getBukkitEntity().getWorld().equals(location.getWorld())) {
            Bukkit.getLogger().log(Level.SEVERE,
                    "[METHOD moveTo] The Npc " + this.hashCode() + " cannot move to the location \"" + locationToString(location) + "\" because "
                            + this.hashCode() + " is in world " + npcPlayer.getBukkitEntity().getWorld().getName()
                            + " and the location provided is in world " + location.getWorld().getName()
            );
        } else {
            // figure out if npc player can use moverel packet
            Vector vR = location.toVector().subtract(npcPlayer.getBukkitEntity().getLocation().toVector());
            boolean canMoverel = location.distance(npcPlayer.getBukkitEntity().getLocation()) < 8;
            if (canMoverel) {
                // use move relative packet
                sendPacket(PacketType.NPC_MOVE, location.getWorld().getPlayers(), new Object[] { vR.getX(), vR.getY(), vR.getZ() });
            } else {
                // use teleport packet
                npcPlayer.setLocation(location.getX(), location.getY(), location.getZ(), npcPlayer.yaw, npcPlayer.pitch);
                sendPacket(PacketType.NPC_TELEPORT, location.getWorld().getPlayers());
            }
        }
    }

    public void attack(LivingEntity livingEntity) {
        if (livingEntity.getLocation().distance(npcPlayer.getBukkitEntity().getLocation()) > 3.0f) {
            livingEntity.damage(1.5d, npcPlayer.getBukkitEntity());
        } else {
            npcPlayer.getBukkitEntity().attack(livingEntity);
            sendPacket(PacketType.NPC_ARM_SWING, livingEntity.getWorld().getPlayers());
        }
    }

    public void moveStepped(Location location) {
        if (!npcPlayer.getBukkitEntity().getWorld().equals(location.getWorld())) {
            Bukkit.getLogger().log(Level.SEVERE,
                    "[METHOD moveStepped] The Npc " + this.hashCode() + " cannot move to the location \"" + locationToString(location) + "\" because "
                            + this.hashCode() + " is in world " + npcPlayer.getBukkitEntity().getWorld().getName()
                            + " and the location provided is in world " + location.getWorld().getName()
            );
        } else {
            Vector vR = location.toVector().subtract(npcPlayer.getBukkitEntity().getLocation().toVector()).normalize(); // normalize into step (movement <= 1 unit)
            sendPacket(PacketType.NPC_MOVE, location.getWorld().getPlayers(), new Object[] { vR.getX(), vR.getY(), vR.getZ() });
        }
    }

    public static List<Npc> getAllNpcs() {
        return allNpcs;
    }

    public boolean isSpawnedIn() {
        return spawnedIn;
    }

    private BukkitRunnable npcLogicLoop() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (isSpawnedIn()) {
                    abilityBooleanHashMap.forEach((k, v) -> {
                        if (v) doNpcAbility(k);
                    });
                }
            }
        };
    }

    private void doNpcAbility(NpcAbility npcAbility) {
        switch (npcFilterMode) {
            case CLOSEST_PLAYER_ONLY:
                switch (npcAbility) {
                    case ATTACK:
                        attack(getClosestPlayer(npcPlayer.getBukkitEntity()));
                        break;
                    case FOLLOW:
                        moveStepped(getClosestPlayer(npcPlayer.getBukkitEntity()).getLocation());
                        break;
                }
                break;
            default:
            case CLOSEST_PLAYER_OR_ENTITY:
                switch (npcAbility) {
                    case ATTACK:
                        attack(getClosestEntity(npcPlayer.getBukkitEntity().getLocation()));
                        break;
                    case FOLLOW:
                        moveStepped(getClosestEntity(npcPlayer.getBukkitEntity().getLocation()).getLocation());
                        break;
                }
                break;
        }
    }

    private void sendPacket(PacketType packetType, Collection<Player> players, Object[] packetArgs) {
        try {
            List<Packet<?>> packetList = new ArrayList<>();
            switch (packetType) {
                case NPC_MOVE:
                    double x = (double)packetArgs[0], y = (double)packetArgs[1], z = (double)packetArgs[2];
                    packetList.add(new PacketPlayOutEntity.PacketPlayOutRelEntityMove(npcPlayer.getId(), (short)(x * 4096), (short)(y * 4096), (short)(z * 4096), true));
                    break;
                case NPC_TELEPORT:
                    packetList.add(new PacketPlayOutEntityTeleport(npcPlayer));
                    break;
                case NPC_ROTATION:
                    float yaw = (float)packetArgs[0], pitch = (float)packetArgs[1];
                    packetList.add(new PacketPlayOutEntityHeadRotation(npcPlayer, (byte)(yaw * 256 / 360)));
                    packetList.add(new PacketPlayOutEntity.PacketPlayOutEntityLook(npcPlayer.getId(), (byte)(yaw * 256 / 360), (byte)(pitch * 256 / 360), true));
                    break;
                case NPC_DELETION:
                    packetList.add(new PacketPlayOutEntityDestroy(npcPlayer.getId()));
                    break;
                case NPC_CREATION:
                    packetList.add(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER));
                    packetList.add(new PacketPlayOutNamedEntitySpawn(npcPlayer));
                    packetList.add(new PacketPlayOutEntityHeadRotation(npcPlayer, (byte) (npcPlayer.yaw * 256 / 360) ));
                    break;
                case NPC_PLAYER_JOIN: // NPC_PLAYER_JOIN is a static/global enum used for when a player joins AFTER an npc is summoned. ALL npcs will appear for the player, so use carefully
                    getAllNpcs().forEach(npc -> npc.sendPacket(PacketType.NPC_CREATION, players));
                    break;
                case NPC_ARM_SWING:
                    packetList.add(new PacketPlayOutAnimation(npcPlayer, 0));
                    break;
            }
            for (Player player : players) {
                for (Packet<?> packet : packetList) {
                    getConnection(player).sendPacket(packet);
                }
            }
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, "FAILED TO SEND PACKET! " + ex.getMessage());
        }
    }

    private void sendPacket(PacketType packetType, Collection<Player> players) {
        sendPacket(packetType, players, new Object[]{});
    }

    private void sendPacket(PacketType packetType, Player player, Object[] packetArgs) {
        sendPacket(packetType, Collections.singleton(player), packetArgs);
    }

    private void sendPacket(PacketType packetType, Player player) {
        sendPacket(packetType, Collections.singleton(player), new Object[]{});
    }

    public PlayerConnection getConnection(Player player) {
        return ((CraftPlayer)player).getHandle().playerConnection;
    }

    public void addAbility(NpcAbility npcAbility) {
        abilityBooleanHashMap.put(npcAbility, true);
    }

    public void removeAbility(NpcAbility npcAbility) {
        abilityBooleanHashMap.put(npcAbility, false);
    }

    public void removeAllAbilities() {
        abilityBooleanHashMap.clear();
    }

}
