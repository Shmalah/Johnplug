package me.shufy.john.util;

import me.shufy.john.DebugCommands;
import me.shufy.john.randomevents.npc.Npc;
import me.shufy.john.randomevents.npc.PacketType;
import net.minecraft.server.v1_16_R2.*;
import org.apache.commons.lang.IllegalClassException;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class JohnUtility {
    public static final UUID JOHN_UUID = UUID.fromString("4523497a-d9af-4b8d-ae8a-33400fdb92d6");
    public JohnUtility() {
      //  throw new IllegalClassException("A non static instance of JohnUtility was created.. JohnUtility is supposed to be a static final class.");
    }
    public static int randomInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }
    public static int randomInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }
    public static boolean randomChance(double percentChance) {
        // will always return true if debug mode is enabled
        return (ThreadLocalRandom.current().nextDouble() < percentChance) || DebugCommands.isDebugMode();
    }
    public static LivingEntity getClosestEntity(Location from) {
        LivingEntity closestLivingEntity = null;
        for (LivingEntity livingEntity : from.getWorld().getLivingEntities()) {
            if (closestLivingEntity == null || locFromToDistance(from, livingEntity.getLocation()) < locFromToDistance(from, closestLivingEntity.getLocation())) {
                closestLivingEntity = livingEntity;
            }
        }
        return closestLivingEntity;
    }
    public static LivingEntity getClosestEntity(Location from, LivingEntity origin) {
        LivingEntity closestLivingEntity = null;
        for (LivingEntity livingEntity : from.getWorld().getLivingEntities()) {
            if (closestLivingEntity == null || locFromToDistance(from, livingEntity.getLocation()) < locFromToDistance(from, closestLivingEntity.getLocation())) {
                if (!livingEntity.equals(origin))
                    closestLivingEntity = livingEntity;
            }
        }
        return closestLivingEntity;
    }
    public static Player getClosestPlayer(Player from) {
        Player closestPlayer = null;
        for (Player player : from.getWorld().getPlayers()) {
            if (closestPlayer == null || playerFromToDistance(from, player) < playerFromToDistance(from, closestPlayer)) {
                closestPlayer = player;
            }
        }
        return closestPlayer;
    }
    public static boolean itemHasLore(ItemStack itemStack) {
        try {
            return (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore());
        } catch (NullPointerException ex) {
            return false;
        }
    }
    public static String getLoreEntry(ItemStack itemStack, int loreEntryIndex) {
        if (!itemHasLore(itemStack)) {
            throw new NullPointerException("getLoreEntry can't get the lore entry for the itemStack provided \"" + itemStack.getType().name() + "\": ITEM META: " + (!itemStack.hasItemMeta() ? "NULL" : "YES") + ",  ITEM LORE: " + (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() ? "YES" : "NULL"));
        } else {
            return itemStack.getItemMeta().getLore().get(loreEntryIndex);
        }
    }
    public static Vector vectorFromPlayerToPlayer(Player from, Player to) {
        return vectorFromEntityToEntity(from, to);
    }
    public static Vector vectorFromEntityToEntity(LivingEntity from, LivingEntity to) {
        return vectorFromLocToLoc(from.getLocation(), to.getLocation());
    }
    public static Vector vectorFromLocToLoc(Location from, Location to) {
        return to.toVector().subtract(from.toVector());
    }
    public static double playerFromToDistance(Player from, Player to) {
        return vectorFromPlayerToPlayer(from, to).length();
    }
    public static double locFromToDistance(Location from, Location to) {
        return vectorFromLocToLoc(from, to).length();
    }
    public static Player randomPlayer(World world) {
        ArrayList<Player> worldPlayers = new ArrayList<>(world.getPlayers());
        if (worldPlayers.isEmpty()) {
            Bukkit.getLogger().log(Level.WARNING, "Players in world is empty.. Are they dead or not in the game?");
            if (Bukkit.getOnlinePlayers().size() > 0) {
                Bukkit.getLogger().log(Level.WARNING, "Found player in different world.");
                return (Player) Bukkit.getOnlinePlayers().toArray()[0];
            } else {
                Bukkit.getLogger().log(Level.WARNING, "Nobody is in the server. Cannot fetch a random player");
                return null;
            }
        }
        return worldPlayers.stream().skip(randomInt(worldPlayers.size())).findFirst().get();
    }
    public static double randomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }
    public static Sound randomSoundWhoContains(String contains) {
        final String containsStr = contains.toLowerCase();
        List<Sound> matchingSounds = Arrays.stream(Sound.values()).filter(sound -> sound.name().toLowerCase().contains(containsStr)).collect(Collectors.toList());
        return (Sound) matchingSounds.toArray()[randomInt(matchingSounds.size())];
    }
    public static Material randomMaterialWhoContains(String contains) {
        final String containsStr = contains.toLowerCase();
        List<Material> matchingMaterials = Arrays.stream(Material.values()).filter(mat -> mat.name().toLowerCase().contains(containsStr)).collect(Collectors.toList());
        return (Material) matchingMaterials.toArray()[randomInt(matchingMaterials.size())];
    }
    public static EntityType randomAnimal() {
        World w = Bukkit.getWorlds().get(0);
        List<EntityType> animalTypes = new ArrayList<>();
        for (EntityType type : EntityType.values()) {
            try {
                Entity e = w.spawnEntity(new Location(w,0, 0, 0), type);
                if (e instanceof Animals) {
                    animalTypes.add(type);
                }
                e.remove();
            } catch (IllegalArgumentException ex) {
                // go to next
            }
        }
        return animalTypes.stream().skip(randomInt(animalTypes.size())).findFirst().get();
    }
    public static World getWorldWithMostPlayers() {
        World mostPlayers = null;
        for (World world : Bukkit.getWorlds()) {
           if (mostPlayers == null) {
               mostPlayers = world;
               continue;
           }
           if (world.getPlayers().size() > mostPlayers.getPlayers().size()) {
               mostPlayers = world;
           }
        }
        return mostPlayers;
    }
    public static String locationToString(Location location) {
        return location.getX() + " " + location.getY() + " " + location.getZ();
    }
    public static void sendNmsPackets(Collection<Player> players, EntityPlayer npc, Object[] packetArgs, PacketType... packetTypes) {
        for (int i = 0; i < packetTypes.length; i++) {
            for (Player player : players) {
                sendPackets(packetTypes[i], npc, player, packetArgs);
            }
        }
    }
    private static void sendPackets(PacketType packetType, EntityPlayer npc, Player player, Object[] packetArgs) {
        ArrayList<Packet<?>> packets = new ArrayList<>();
        switch (packetType) {
            case NPC_MOVE:
                double x = (double)packetArgs[0], y = (double)packetArgs[1], z = (double)packetArgs[2];
                packets.add(new PacketPlayOutEntity.PacketPlayOutRelEntityMove(npc.getId(), (short)(x * 4096), (short)(y * 4096), (short)(z * 4096), true));
                break;
            case NPC_TELEPORT:
                packets.add(new PacketPlayOutEntityTeleport(npc));
                break;
            case NPC_ROTATION:
                float yaw = (float)packetArgs[0], pitch = (float)packetArgs[1];
                npc.yaw = yaw;
                packets.add(new PacketPlayOutEntityHeadRotation(npc, (byte)(yaw * 256 / 360)));
                npc.pitch = pitch;
                packets.add(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte)(yaw * 256 / 360), (byte)(pitch * 256 / 360), true));
                break;
            case NPC_DELETION:
                packets.add(new PacketPlayOutEntityDestroy(npc.getId()));
                break;
            case NPC_CREATION:
                packets.add(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
                packets.add(new PacketPlayOutNamedEntitySpawn(npc));
                packets.add(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
                break;
            case NPC_ARM_SWING:
                break;
            case NPC_PLAYER_JOIN:
                Npc.allNpcs.forEach(NPC -> sendPackets(PacketType.NPC_CREATION, npc, player, new Object[]{}));
                return;
        }
        if (!packets.isEmpty())
            for (Packet<?> packet : packets) ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
    public static String bold(ChatColor chatColor) {
        return chatColor.toString() + ChatColor.BOLD;
    }
}
