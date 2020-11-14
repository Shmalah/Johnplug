package me.shufy.john.util;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;

public class PacketSender {

    public enum PacketTypeEnum {
        NPC_ADDED,
        NPC_REMOVED,
        NPC_TELEPORTED,
        NPC_ROTATED,
        NPC_ANIMATION
    }

    public static void sendNmsPacket(Packet<?> packet, Collection<Player> players) {
        for (Player player : players) {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }
    }
    public static void sendNmsPacket(PacketTypeEnum packetType, Collection<Player> players, EntityPlayer npc) {
        if (npc == null) {
            Bukkit.getLogger().log(Level.SEVERE, " [!!!] Tried to send an nms packet through parameter PacketTypeEnum but the npc given was null.");
            return;
        }
        switch (packetType) {
            case NPC_ADDED:
                sendNmsPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc), players);
                sendNmsPacket(new PacketPlayOutNamedEntitySpawn(npc), players);
                sendNmsPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)), players);
                break;
            case NPC_REMOVED:
                sendNmsPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc), players);
                sendNmsPacket(new PacketPlayOutEntityDestroy(npc.getId()), players);
                break;
            case NPC_TELEPORTED:
                sendNmsPacket(new PacketPlayOutEntityTeleport(npc), players);
                break;
            case NPC_ROTATED:
                sendNmsPacket(new PacketPlayOutEntityHeadRotation(npc, (byte)(npc.yaw * 256 / 360)), players);
                sendNmsPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte)(npc.yaw * 256 / 360), (byte)(npc.pitch * 256 / 360), true), players);
                break;
            case NPC_ANIMATION:
                sendNmsPacket(new PacketPlayOutAnimation(npc, 0), players);
                break;
        }
    }

    // overload
    public static void sendNmsPacket(Packet<?> packet, Player player) {
        sendNmsPacket(packet, Collections.singleton(player));
    }
    // overload
    public static void sendNmsPacket(PacketTypeEnum packetType, Player player, EntityPlayer optionalNpc) {
        sendNmsPacket(packetType, Collections.singleton(player), optionalNpc);
    }

}
