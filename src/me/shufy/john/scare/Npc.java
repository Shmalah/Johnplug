package me.shufy.john.scare;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.shufy.john.Main;
import me.shufy.john.util.PacketSender;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Npc {

    private static final List<Npc> allNpcs = new ArrayList<>();
    private final World npcWorld;
    private EntityPlayer npcPlayer;

    public static GameProfile johnProfile() {
        // Skin #169665813 generated on Nov 11, 2020 3:25:33 PM via MineSkin.org - https://minesk.in/169665813
        GameProfile skin169665813 = new GameProfile(UUID.fromString("a6bc2231-fe68-4b75-b796-b074b763410b"), "John");
        skin169665813.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYwNTEzMzUzMzE4OSwKICAicHJvZmlsZUlkIiA6ICI4MmM2MDZjNWM2NTI0Yjc5OGI5MWExMmQzYTYxNjk3NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3ROb3RvcmlvdXNOZW1vIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzU3ZjMyYjEwOGMxNjZhNWNkNmI4YWQzOTA3ZTg3MWMyYjYyMTRmY2VhMDk2YzZkMTY5YjUyNDhhNDg3Y2NiYjQiCiAgICB9CiAgfQp9", "ALNkre7PT/ufdze5/fFA9cBDwTX/xtO7l38rn/VxjjW2qd0pQiBw09fYYmmlhUWRf3ohr7JbrfLho4fu09WpZGn4+mgHVg2VqOalqFySiG8MX4WcI6whYfjcn6ijLAmfTcaDY9pph+JQHwzi8V1ByvcqLQldzBt/SD9oxvc1PqN3KCPstzfIYEJGeLis7M6gc+qaI7xCu/gKOiNcyGLvKUu5InDgPgtw/7DBPs6a1gaY6yutG7yJDsQqBaFyERbsOdwNytsiRehvJ/JaWRznO0XDrXYA+GiNKaRP6ehpiHuRIFENEIIMBRncjjL+Xj3/TS3oANjPBHCmeq0q8Z05v0N5vPvO8vyJWYdw5YLPfubePGsf2n/KiGYroe3n7aohViwzBIzGUVNTxcHchMUI7X/RtMSm3y8iuo5ByLcRvxKU6Jn/vnI3qCYGLqIf/+TnzwseUl5FvMMZfezVOCEU5tzZwBriDIn7x5dcBTu1Z2qVXe4hIatpkUCPlq+kREkaJu7VCs7AiP4hWFvdFfyj+TkFvunzNkUd5Tf3SRQYxlJaD2niHfAzlH/sR6u3N5WZLeGGe6R+LmglSHmT8vIhw/YbvwlQyht/rDmiyA7jT+BekEB8bjFRLx4gd+2N6qLJBZhB8UbeAhyJppG0nVafbKGjw14YU8nW4EbnQOfaojw="));
        return skin169665813;
    }

    public Npc(World world) {
        makeNpc(world);
        this.npcWorld = world;
        getAllNpcs().add(this);
    }

    public static void initializeNpcs() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!getAllNpcs().isEmpty())
                    getAllNpcs().removeIf(Objects::isNull);
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, (20L * 5));
    }

    public Npc() {
        this(Bukkit.getWorlds().get(0));
    }

    private void makeNpc(World world) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld)world).getHandle();
        setNpcPlayer(new EntityPlayer(server, worldServer, johnProfile(), new PlayerInteractManager(worldServer)));
        getNpcPlayer().setCustomNameVisible(false);
    }

    public void appearTo(Player player) {
        double distance = ThreadLocalRandom.current().nextInt(2, 5);
        Location appearLocation = player.getLocation();
        switch (player.getFacing()) {
            case NORTH:
                appearLocation.setZ(appearLocation.getZ() - distance);
                break;
            case SOUTH:
                appearLocation.setZ(appearLocation.getZ() + distance);
                break;
            case WEST:
                appearLocation.setX(appearLocation.getX() - distance);
                break;
            case EAST:
                appearLocation.setX(appearLocation.getX() + distance);
                break;
        }
        // chance that john will appear one block above so that you can see his actual face (his body)
        if (ThreadLocalRandom.current().nextDouble() < 0.2d)
            appearLocation.setY(appearLocation.getY() + 1)
                    ;
        spawn(appearLocation, player);
    }

    public void lookAt(Player player) {
        Vector vR = player.getLocation().toVector().subtract(getNpcPlayer().getBukkitEntity().getLocation().toVector()).normalize();
        float yaw = (float) Math.toDegrees(Math.atan2(vR.getZ(), vR.getX()))-90;
        float pitch = (float) Math.toDegrees(Math.asin(vR.getY()))*-1;
        getNpcPlayer().yaw = yaw;
        getNpcPlayer().pitch = pitch;
        PacketSender.sendNmsPacket(PacketSender.PacketTypeEnum.NPC_ROTATED, player, getNpcPlayer());
    }

    public void spawn(Location location, Player player) {
        if (getNpcPlayer() != null && player.isOnline() && !player.isDead()) {
            getNpcPlayer().setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            PacketSender.sendNmsPacket(PacketSender.PacketTypeEnum.NPC_ADDED, player, getNpcPlayer());
        }
    }

    public void remove(Player player) {
        if (getNpcPlayer() != null && player.isOnline() && !player.isDead()) {
            PacketSender.sendNmsPacket(PacketSender.PacketTypeEnum.NPC_REMOVED, player, getNpcPlayer());
        }
    }

    public static void deinitializeNpcs() {
        for (Npc allNpc : getAllNpcs()) {
            if (allNpc != null) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    PacketSender.sendNmsPacket(PacketSender.PacketTypeEnum.NPC_REMOVED, onlinePlayer, allNpc.getNpcPlayer());
                }
            }
        }
    }

    public EntityPlayer getNpcPlayer() {
        return npcPlayer;
    }

    public static List<Npc> getAllNpcs() {
        return allNpcs;
    }

    private void setNpcPlayer(EntityPlayer npc) {
        this.npcPlayer = npc;
    }

    public World getNpcWorld() {
        return npcWorld;
    }
}
