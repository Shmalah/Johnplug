package me.shufy.john.events.mlg;

import me.shufy.john.corenpc.JohnNpc;
import me.shufy.john.events.JohnEvent;
import me.shufy.john.player.BlockLogger;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import static me.shufy.john.util.JohnUtility.bold;
import static me.shufy.john.util.JohnUtility.randomLocationNearPlayer;

public class MlgEvent extends JohnEvent {

    public Collection<Player> deathList;
    public Collection<Player> winList;

    public MlgEvent(World eventWorld, int duration, double chance) {
        super(eventWorld, "MlgEvent", bold(ChatColor.BLUE) + "MLG EVENT", "Land the MLG water bucket or face harsh consequences.", duration, chance);
    }

    @Override
    public void onEventCountdownStart() {
        deathList = new ArrayList<>();
        winList = new ArrayList<>();
        getPlayers().forEach(player ->  {
            if (!player.isDead()) {
                player.setGameMode(GameMode.SURVIVAL);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.sendMessage(bold(ChatColor.YELLOW) + "Even if you die before the event even starts, you LOSE!");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1f, ThreadLocalRandom.current().nextFloat());
            }
        });
        getPlayers().removeIf(p -> p.isDead() || !p.isOnline());
    }

    @Override
    public void onEventStart() {
        getPlayers().forEach(player -> {
            if (!deathList.contains(player)) {
                if (player.getLocation().getY() < getEventWorld().getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ()))
                    player.teleport(getEventWorld().getHighestBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ()).getLocation());
                player.teleport(player.getLocation().add(0, ThreadLocalRandom.current().nextInt(150, 300), 0));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // give player the mlg water bucket at a random time
                        player.getInventory().addItem(mlgWaterBucket());
                    }
                }.runTaskLater(plugin, ThreadLocalRandom.current().nextInt(1, 5) * 20L);
            }
        });
    }

    @Override
    public void everyEventTick() {
        getPlayers().forEach(player -> {
            // ANTI CHEAT! :)
            if (playerInEvent(player)) {
                Block blockBelowPlayer = getEventWorld().getHighestBlockAt(player.getLocation());
                if (!BlockLogger.getPlayerBlocks(player).contains(blockBelowPlayer) || (BlockLogger.getPlayerBlocks(player).contains(blockBelowPlayer) && player.getLocation().distance(blockBelowPlayer.getLocation()) > 20)) {
                    if (blockBelowPlayer.isLiquid()) {
                        // They're trying to land in water or somethn that is not theirs
                        // make a square of bedrock
                        for (int x = 0; x < 2; x++) {
                            for (int z = 0; z < 2; z++) {
                                blockBelowPlayer.getRelative(x, 0, z).setType(Material.BEDROCK);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onCountdownAnnounce(int secondsLeft) {
        broadcastMsg(bold(ChatColor.RED) + secondsLeft + " seconds left to land the " + bold(ChatColor.BLUE) + "MLG!");
    }

    @Override
    public void onEventEndCountdownStart() {
        broadcastMsg(bold(ChatColor.BLUE) + "You don't have much time left!", false);
    }

    @Override
    public void onEventEnd() {
        broadcastMsg(bold(ChatColor.YELLOW) + "THE EVENT HAS CONCLUDED. LOSERS WILL BE PUNISHED.");
        getPlayers().forEach(player -> {
            if (deathList.contains(player) && !winList.contains(player)) {
                if (player.isDead() || !player.isOnline()) {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), bold(ChatColor.RED) + "Ur banned for 30 seconds", null, null);
                    new BukkitRunnable() {
                        private int secondsLeft = 30;
                        @Override
                        public void run() {
                            secondsLeft--;
                            Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), bold(ChatColor.RED) + "Ur banned for " + secondsLeft + " seconds for JOHN LOGGING.", null, null);
                            player.kickPlayer( bold(ChatColor.RED) + "Ur banned for " + secondsLeft + " seconds for JOHN LOGGING.");
                            if (secondsLeft == 1) {
                                Bukkit.getBanList(BanList.Type.NAME).pardon(player.getName());
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 0, 20L);
                } else {
                    player.sendMessage(bold(ChatColor.DARK_RED) + "JOHN IS DISAPPOINTED IN YOU FOR LOSING THE EVENT.");
                    player.getWorld().strikeLightning(player.getLocation());
                    JohnNpc npc = new JohnNpc(randomLocationNearPlayer(player, 10));
                    npc.spawn(Collections.singleton(player));
                    npc.targetPlayer(player);
                    new BukkitRunnable() {
                        private int ticks = 0;
                        @Override
                        public void run() {
                            if (player.isDead() || ticks > 200) {
                                npc.destroy();
                                this.cancel();
                            }
                            ticks++;
                        }
                    }.runTaskTimer(plugin, 0, 1L);
                }
            } else if (deathList.contains(player) && winList.contains(player)) {
                Bukkit.getLogger().log(Level.SEVERE, String.format("Player %s is in both winner list and loser list! This isn't supposed to happen.", player.getName()));
                Bukkit.getLogger().log(Level.INFO, "LOSERS: " + deathList.toString());
                Bukkit.getLogger().log(Level.INFO, "WINNERS: " + winList.toString());
            }
            player.getInventory().remove(Material.BUCKET);
            player.getInventory().remove(Material.WATER_BUCKET);
            player.getInventory().remove(Material.LAVA_BUCKET);
            winList.clear();
            deathList.clear();
        });
    }

    public boolean playerInEvent(Player player) {
        return (!winList.contains(player) && !deathList.contains(player));
    }

    private ItemStack mlgWaterBucket() {
        ItemStack mlgBucket = new ItemStack(Material.WATER_BUCKET);
        ItemMeta mlgMeta = mlgBucket.getItemMeta();
        mlgMeta.setDisplayName(bold(ChatColor.BLUE) + "MLG WATER BUCKET");
        mlgMeta.setLore(new ArrayList<String>() {{
            add (ChatColor.GOLD + "The official MLG water bucket. Stop looking at this and land your mlg idiot");
        }});
        mlgMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        mlgBucket.setItemMeta(mlgMeta);
        return mlgBucket;
    }
}
