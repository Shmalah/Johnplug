package me.shufy.john.util;

import me.shufy.john.DebugCommands;
import org.apache.commons.lang.IllegalClassException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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
        return worldPlayers.stream().skip(randomInt(worldPlayers.size())).findFirst().get();
    }
    public static String bold(ChatColor chatColor) {
        return chatColor.toString() + ChatColor.BOLD;
    }
}
