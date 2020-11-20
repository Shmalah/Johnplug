package me.shufy.john.util.john;

import me.shufy.john.DebugCommands;
import me.shufy.john.Main;
import me.shufy.john.util.structs.SoundInfo;
import me.shufy.john.util.structs.YawPitch;
import me.shufy.john.util.world.ParticleRay;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class JohnUtility {
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
    public static Player getClosestPlayer(Location loc) {
        if (loc.getWorld().getPlayers().isEmpty())
            return null;
        Player closest = null;
        for (Player player : loc.getWorld().getPlayers()) {
            if (closest == null || player.getLocation().distance(loc) < closest.getLocation().distance(loc)) {
                if (!player.isDead())
                    closest = player;
            }
        }
        return closest;
    }

    /**
     * 
     * @param origin
     * <p>The center of the search</p>
     * @param accuracy
     * <p>The accuracy of the search. 1 is the best accuracy</p>
     * @return
     * <p>The block if found, else null</p>
     */
    public static Block explodeSearchBlock(Location origin, Vector vector, Material target, int accuracy) {
        for (int i = 0; i < 360/accuracy; i++) {
            RayTraceResult rt = origin.getBlock().rayTrace(origin.getBlock().getLocation(), vector.rotateAroundAxis(upVector(), i).normalize(), 100, FluidCollisionMode.SOURCE_ONLY);
            if (rt != null) {
                if (rt.getHitBlock() != null) {
                    if (rt.getHitBlock().getType().equals(target))
                        return rt.getHitBlock();
                }
            }
        }
        return null;
    }

    public static Block findBlockWithMaterialInLOS(Player player, Material material) {
        for (Block block : player.getLineOfSight(null, 100))
            if (block.getType().equals(material))
                return block;
        return null;
    }

    public static Block findBlockWithMaterialContainsInLOS(Player player, String contains) {
        for (Block block : player.getLineOfSight(null, 100))
            if (block.getType().name().contains(contains.toUpperCase()))
                return block;
        return null;
    }

    public static Block explodeSearchBlockContains(Location origin, Vector vector, String contains, int accuracy) {
        for (int i = 0; i < 360/accuracy; i++) {
            // debug
            ParticleRay particleRay = new ParticleRay(origin,  vector.rotateAroundAxis(upVector(), i), 3, Color.ORANGE, 2);
            particleRay.draw();
            RayTraceResult rt = origin.getBlock().rayTrace(origin.getBlock().getLocation(), vector.rotateAroundAxis(upVector(), i), 100, FluidCollisionMode.SOURCE_ONLY);
            if (rt != null) {
                if (rt.getHitBlock() != null) {
                    if (rt.getHitBlock().getType().name().contains(contains.toUpperCase()))
                        return rt.getHitBlock();
                }
            }
        }
        return null;
    }

    public static Vector upVector() {
        return new Vector(0, 1, 0);
    }

    public List<Block> getBlocks(Location pos1, Location pos2)
    {
        if(pos1.getWorld() != pos2.getWorld())
            return null;
        World world = pos1.getWorld();
        List<Block> blocks = new ArrayList<>();
        int x1 = pos1.getBlockX(), y1 = pos1.getBlockY(), z1 = pos1.getBlockZ();
        int x2 = pos2.getBlockX(), y2 = pos2.getBlockY(), z2 = pos2.getBlockZ();
        int lowestX = Math.min(x1, x2), lowestY = Math.min(y1, y2), lowestZ = Math.min(z1, z2);
        int highestX = lowestX == x1 ? x2 : x1, highestY = lowestX == y1 ? y2 : y1, highestZ = lowestX == z1 ? z2 : z1;
        for(int x = lowestX; x <= highestX; x++)
            for(int y = lowestY; x <= highestY; y++)
                for(int z = lowestZ; x <= highestZ; z++)
                    blocks.add(world.getBlockAt(x, y, z));
        return blocks;
    }

    public static Player getClosestPlayerExclusive(Player exFrom) {
        Player closest = getClosestPlayer(exFrom);
        if (closest == null || closest.equals(exFrom)) {
            return exFrom;
        }
        return closest;
    }
    public static LivingEntity getClosestEntityExclusive(Player exFrom) {
        LivingEntity closest = getClosestEntity(exFrom.getLocation());
        if (closest == null || closest.equals(exFrom)) {
            return null;
        }
        return closest;
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
    public static World getRandomWorld() {
        return Bukkit.getWorlds().stream().filter(w -> !w.getPlayers().isEmpty()).skip(ThreadLocalRandom.current().nextInt(Bukkit.getWorlds().size())).findFirst().orElse(null);
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
    public static boolean playerIsTargeting(Player player, Location target) {
        if(!target.getWorld().equals(player.getWorld()))
            return false;

        Location head = player.getLocation().add(0, player.getEyeHeight(), 0);

        Vector look = player.getLocation().getDirection().normalize();

        Vector direction = head.subtract(target).toVector().normalize();

        Vector cp = direction.crossProduct(look);

        double length = cp.length();

        // temporary debug
        Bukkit.getLogger().log(Level.INFO, String.valueOf(cp.length()));

        return (length < 0.1);
    }

    public static final Main plugin = Main.getPlugin(Main.class);

    public static boolean randomChanceNoDebug(double percentChance) {
        return ThreadLocalRandom.current().nextDouble() < percentChance;
    }

    public static void johnBan(Player player, int seconds) {
        if (seconds <= 0)
            return;
        boolean playerBanned = Bukkit.getBanList(BanList.Type.NAME).getBanEntries().stream().anyMatch(banEntry -> banEntry.getTarget().equals(player.getName()));
        if (!playerBanned) {
            // ban player
            Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), bold(ChatColor.BOLD) + "BANNED FOR " + seconds + " SECONDS FOR JOHN LOGGING!", null, null);
            player.kickPlayer(bold(ChatColor.RED) + "BANNED FOR " + seconds + " SECONDS FOR JOHN LOGGING");
            new BukkitRunnable() {
                private int secondsLeft = seconds;
                @Override
                public void run() {
                    // update ban time every second
                    secondsLeft--;
                    Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), bold(ChatColor.BOLD) + String.format("BANNED FOR %s SECONDS FOR JOHN LOGGING!", secondsLeft), null, null);
                    if (secondsLeft == 1) {
                        Bukkit.getBanList(BanList.Type.NAME).pardon(player.getName());
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 20L);
        }
    }

    public static boolean loreContains(ItemStack item, String contains) {
        if (item == null)
            return false;
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore() || item.getItemMeta().getLore().isEmpty())
            return false;
        for (String line : item.getItemMeta().getLore()) {
            if (line.contains(contains))
                return true;
        }
        return false;
    }
    public static void broadcastSound(Sound sound, SoundInfo volumeAndPitch) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.getWorld().playSound(onlinePlayer.getLocation(), sound, volumeAndPitch.volume, volumeAndPitch.pitch);
        }
    }
    public static Location randomLocationNearPlayer(Player player, int withinRadius) {
        return player.getLocation().add(randomInt(withinRadius), randomInt(withinRadius), randomInt(withinRadius));
    }
    public static String locationToString(Location location) {
        return location.getX() + " " + location.getY() + " " + location.getZ();
    }
    public static Biome getPlayerBiome(Player player) {
        return player.getWorld().getBiome(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
    }
    /*
    public static void sendNmsPackets(Collection<Player> players, EntityPlayer npc, Object[] packetArgs, PacketType... packetTypes) {
        for (int i = 0; i < packetTypes.length; i++) {
            for (Player player : players) {
                sendPackets(packetTypes[i], npc, player, packetArgs);
            }
        }
    }

     */
  /*  private static void sendPackets(PacketType packetType, EntityPlayer npc, Player player, Object[] packetArgs) {
        ArrayList<Packet<?>> packets = new ArrayList<>();
        switch (packetType) {
            case NPC_MOVE: // this never works
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
                int armToSwing = (int) packetArgs[0];
                packets.add(new PacketPlayOutAnimation(npc, armToSwing)); // 0 aka main hand, 1 aka off-hand ?
                break;
            case NPC_PLAYER_JOIN:
                Npc.allNpcs.forEach(NPC -> sendPackets(PacketType.NPC_CREATION, npc, player, new Object[]{}));
                return;
        }
        if (!packets.isEmpty())
            for (Packet<?> packet : packets) ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }*/
    public static YawPitch yawPitchFromVector(Vector vector) {
        float yaw = (float) Math.toDegrees(Math.atan2(vector.getZ(), vector.getX()))-90, pitch = (float) Math.toDegrees(Math.asin(vector.getY()))*-1;
        return new YawPitch(yaw, pitch);
    }
    public static Player closestPlayerToVehicle(Vehicle v) {
        Player closestPlayer = null;
        for (Player player : v.getWorld().getPlayers()) {
            double playerDist = vectorFromLocToLoc(v.getLocation(), player.getLocation()).length();
            if (closestPlayer == null) {
                closestPlayer = player;
            } else {
                double closestDist = vectorFromLocToLoc(v.getLocation(), closestPlayer.getLocation()).length();
                if (playerDist < closestDist) {
                    closestPlayer = player;
                }
            }
        }
        return closestPlayer;
    }
    /**
     * <p>Creates a default length-by-height 4 x 4 nether portal.</p>
     * @param frameLocation
     * <p>Refers to where the bottom left corner of the portal frame will reside when created.</p>
     * @param frontFacingDirection
     * <p>The direction that the front of the portal will face when created</p>
     * @param fillCorners
     * <p>During creation, whether to fill the corners of the nether portal with obsidian or to keep them empty</p>
     */
    public static void createNetherPortal(Location frameLocation, BlockFace frontFacingDirection, boolean fillCorners) {

    }
    public static String bold(ChatColor chatColor) {
        return chatColor.toString() + ChatColor.BOLD;
    }
}
