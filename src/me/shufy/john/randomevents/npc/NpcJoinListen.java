package me.shufy.john.randomevents.npc;

import me.shufy.john.util.JohnUtility;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collections;
import java.util.logging.Level;

public class NpcJoinListen implements Listener {
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        try  {
            JohnUtility.sendNmsPackets(Collections.singleton(e.getPlayer()), null, new Object[]{}, PacketType.NPC_PLAYER_JOIN);
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to send join packet to " + e.getPlayer().getName() + "! Error: " + ex.getMessage());
        }
    }
}
