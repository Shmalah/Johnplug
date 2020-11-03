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

    public static List<Npc> allNpcs = new ArrayList<>();
    public static final Main plugin = Main.getPlugin(Main.class);

    private String displayName;
    private String uuid;
    private Location spawnLocation;
    private NpcFilter npcMode = NpcFilter.CLOSEST_PLAYER_OR_ENTITY; // default

    private EntityPlayer npcPlayer;

    public Npc(String displayName, String uuid, Location spawnLocation) {
        this.displayName = displayName;
        this.uuid = uuid;
        this.spawnLocation = spawnLocation;

        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld)Bukkit.getWorld("world")).getHandle(); // Change "world" to the world the NPC should be spawned in.
        GameProfile gameProfile = new GameProfile(UUID.fromString(uuid), displayName); // Change "playername" to the name the NPC should have, max 16 characters.
        npcPlayer = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld)); // This will be the EntityPlayer (NPC) we send with the sendNPCPacket method.
        npcPlayer.setLocation(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), spawnLocation.getYaw(), spawnLocation.getPitch());
        sendNmsPackets(spawnLocation.getWorld().getPlayers(), npcPlayer, new Object[]{}, PacketType.NPC_CREATION);

        allNpcs.add(this);
        npcLoop().runTaskTimer(plugin, 0, 1L);
    }

    private BukkitRunnable npcLoop() {
        return new BukkitRunnable() {
            @Override
            public void run() {

            }
        };
    }


}
