package me.shufy.john.items.weapons;

import me.shufy.john.Main;
import me.shufy.john.items.JohnItem;
import me.shufy.john.items.JohnableItem;
import me.shufy.john.util.CubicScan;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import static me.shufy.john.items.JohnItem.randomMaterial;
import static me.shufy.john.items.JohnItem.randomMaterialWhichContains;
import static me.shufy.john.util.JohnUtility.*;

public class JohnItemAbilities implements JohnableItem {

    public static final Main plugin = Main.getPlugin(Main.class);

    @Override
    public void onLeftClickBlock(JohnItem.ItemAbility itemAbility, Player player, PlayerInteractEvent playerInteractEvent) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        switch (JohnItem.getItemAbility(heldItem)) {
            default:
            case NONE:
                player.sendMessage(bold(ChatColor.GRAY) + "You can feel tears streaming down your face ;(");
                break;
            case TRAMPOLINE:
                onLeftClickAir(itemAbility, player, playerInteractEvent); // same
                break;
            case AURA:
                onLeftClickAir(itemAbility, player, playerInteractEvent);
                break;
            case LUCK:
                if (randomChance(0.10d)) {
                    Material randOre = randomMaterialWhichContains("ore");
                    Bukkit.getLogger().log(Level.INFO, "Attempted to set clicked block to " + randOre.name());
                    playerInteractEvent.getClickedBlock().getRelative(0, -1, 0).setType(randOre);
                    player.sendMessage(ChatColor.GREEN + "You have been blessed!");
                }
                break;
            case LAZER:
                World w = player.getWorld();
                new BukkitRunnable() {
                    int ticks = 0;
                    @Override
                    public void run() {
                        if (ticks >= 50) this.cancel();
                        if (!player.getWorld().equals(w)) this.cancel(); // they moved worlds since using the ability
                        Vector vDir = player.getLocation().getDirection();
                        if (ticks % 10 == 0) player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_BURN, 0.7f, ThreadLocalRandom.current().nextFloat());
                        RayTraceResult traceResult = w.rayTraceBlocks(player.getEyeLocation(), vDir, 20d);
                        if (traceResult != null) {
                            for (double i = 0.1d; i < traceResult.getHitPosition().length(); i++) {
                                w.spawnParticle(Particle.WHITE_ASH, traceResult.getHitPosition().normalize().multiply(i).toLocation(w), 3);
                            }
                            if (traceResult.getHitBlockFace() != null)
                                traceResult.getHitBlockFace().getDirection().toLocation(w).getBlock().breakNaturally();
                                traceResult.getHitBlockFace().getDirection().toLocation(w).getBlock().setType(Material.FIRE); // sets the block on fire
                            if (traceResult.getHitBlock() != null) {
                                for (BlockFace blockFace : BlockFace.values()) {
                                    traceResult.getHitBlock().getRelative(blockFace).setType(Material.FIRE);
                                }
                            }
                            if (traceResult.getHitEntity() != null) {
                                traceResult.getHitEntity().setVelocity(player.getLocation().getDirection());
                                traceResult.getHitEntity().setFireTicks(20);
                            }
                        }
                        ticks++;
                    }
                }.runTaskTimer(plugin, 0, 1L); // aka every tick
                break;
        }
    }

    @Override
    public void onLeftClickAir(JohnItem.ItemAbility itemAbility, Player player, PlayerInteractEvent playerInteractEvent) {
        switch (itemAbility) {
            default:
            case NONE:
                onLeftClickBlock(itemAbility, player, playerInteractEvent);
                break;
            case TRAMPOLINE:
                player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1f, ThreadLocalRandom.current().nextFloat());
                player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1f, ThreadLocalRandom.current().nextFloat());
                getClosestEntity(player.getLocation(), player).setVelocity(player.getLocation().getDirection().add(new Vector(0, 2, 0)).multiply(2));
                break;
            case AURA:
                Location loc = player.getLocation();
                boolean oreFound = false;
                for (int y = 0; y < 50; y++) {
                    if (oreFound) break;
                    for (int x = -3; x < 3; x++) {
                        if (oreFound) break;
                        for (int z = -3; z < 3; z++) {
                            if (loc.getBlock().getRelative(x, -y, z).getType().name().contains("ORE")) {
                                player.getInventory().addItem(new ItemStack(loc.getBlock().getRelative(x, -y, z).getType()));
                                loc.getBlock().getRelative(x, -y, z).setType(Material.AIR);
                                oreFound = true;
                                break;
                            }
                        }
                    }
                }
                break;
            case LUCK:
                if (randomChance(0.05d)) {
                    player.sendMessage(bold(ChatColor.GREEN) + "You feel a leprechaun ticks your insides."); // lol
                    player.getInventory().addItem(new ItemStack(Material.NETHER_PORTAL));
                }
                break;
            case LAZER:
                onLeftClickBlock(itemAbility, player, playerInteractEvent); // same
                break;
        }
    }

    @Override
    public void onRightClickBlock(JohnItem.ItemAbility itemAbility, Player player, PlayerInteractEvent playerInteractEvent) {
        switch (itemAbility) {
            default:
            case NONE:
                onLeftClickBlock(itemAbility, player, playerInteractEvent);
                break;
            case TRAMPOLINE:
                onRightClickAir(itemAbility, player, playerInteractEvent); // same
                break;
            case AURA:
                playerInteractEvent.setCancelled(true); // stop john item in hand from being placed so they dont lose it!
                Location loc = player.getLocation();
                ArrayList<Block> blocks = new ArrayList<>();
                for (int y = -2; y < 5; y++) {
                    for (int x = -3; x < 3; x++) {
                        for (int z = -3; z < 3; z++) {
                            blocks.add(loc.getBlock().getRelative(x, y, z));
                        }
                    }
                }
                //auraTask =
                new BukkitRunnable() {
                    private int ticksElapsed = 0;
                    @Override
                    public void run() {
                        if (ticksElapsed < 200) {
                            blocks.get(0).breakNaturally();
                            blocks.remove(0);
                        } else {
                            blocks.clear();
                            this.cancel();
                        }
                        ticksElapsed++;
                    }
                }.runTaskTimer(plugin, 0, 1L);
                break;
            case LUCK:
                break;
            case LAZER:
                break;
        }
    }

    @Override
    public void onRightClickAir(JohnItem.ItemAbility itemAbility, Player player, PlayerInteractEvent playerInteractEvent) {
        switch (itemAbility) {
            default:
            case NONE:
                onLeftClickBlock(itemAbility, player, playerInteractEvent);
                break;
            case TRAMPOLINE:
                player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "Boing!! John trampoline");
                player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1f, ThreadLocalRandom.current().nextFloat());
                player.setVelocity(new Vector(0, 1.5, 0));
                break;
            case AURA:
                //BukkitTask auraTask;
                final LivingEntity[] target = {getClosestEntity(player.getLocation(), player)};
                //auraTask =
                new BukkitRunnable() {
                    private int ticksElapsed = 0;
                    @Override
                    public void run() {
                        if ((ticksElapsed % 6 == 0 && ticksElapsed != 0) && ticksElapsed < 80) { // (80 ticks) aka 4 seconds
                            target[0] = getClosestEntity(player.getLocation(), player);
                            target[0].setNoDamageTicks(0);
                            target[0].damage(1.0, player);
                            if (randomChance(0.10d)) {
                                target[0].setFireTicks(20);
                            }
                            target[0].sendMessage(bold(ChatColor.RED) + player.getName() + " used AURA on you!");
                            Bukkit.getLogger().log(Level.INFO, player.getName() + " used AURA (" + ticksElapsed + " ticks) | Target: " + target[0].toString());
                        } else if (ticksElapsed >= 80) {
                            this.cancel();
                        }
                        target[0].getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, target[0].getLocation(), 4);
                        target[0].getWorld().playSound(target[0].getLocation(), Sound.UI_TOAST_OUT, 1f, ThreadLocalRandom.current().nextFloat());
                        ticksElapsed++;
                    }
                }.runTaskTimer(plugin, 0, 1L);
               // Bukkit.getLogger().log(auraTask.isCancelled() ? Level.WARNING : Level.INFO, "Aura ability worker is cancelled: " + auraTask.isCancelled());
                break;
            case LUCK:
                break;
            case LAZER:
                break;
        }
    }
}
