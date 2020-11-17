package me.shufy.john.player;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class BlockLogger implements Listener {

    static HashMap<Player, Set<Block>> playerBlocks = new HashMap<>();

    public BlockLogger() {
        // initialization
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            playerBlocks.putIfAbsent(onlinePlayer, new HashSet<Block>());
        }
    }

    @EventHandler
    public void playerJoinEvent (PlayerJoinEvent e) {
        playerBlocks.putIfAbsent(e.getPlayer(), new HashSet<Block>());
    }

    @EventHandler
    public void blockPlace (BlockPlaceEvent e) {
        addBlockEntry(e.getPlayer(), e.getBlockPlaced());
    }
    @EventHandler
    public void blockBreak (BlockBreakEvent e) {
        removeBlockEntry(e.getPlayer(), e.getBlock());
    }

    private void addBlockEntry(Player player, Block block) {
        playerBlocks.putIfAbsent(player, new HashSet<Block>());
        playerBlocks.get(player).add(block);
    //    Bukkit.getLogger().log(Level.INFO, playerBlocks.get(player).size() + " blocks belong to " + player.getName());
    }

    private void removeBlockEntry(Player player, Block block) {
        if (!playerBlocks.containsKey(player)) {
            Bukkit.getLogger().log(Level.WARNING, String.format("Tried to remove block %s from player %s's block log but there is no key for the player %s", block.getType().name(), player.getName(), player.getName()));
            return;
        }
        playerBlocks.get(player).removeIf(b -> b.equals(block));
    //    Bukkit.getLogger().log(Level.INFO, playerBlocks.get(player).size() + " blocks belong to " + player.getName());
    }

    public static HashMap<Player, Set<Block>> getAllPlayerBlocks() {
        return playerBlocks;
    }

    public static Set<Block> getPlayerBlocks(Player player) {
        return playerBlocks.getOrDefault(player, new HashSet<>());
    }
}
