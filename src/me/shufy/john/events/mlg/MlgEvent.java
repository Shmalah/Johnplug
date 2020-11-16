package me.shufy.john.events.mlg;

import me.shufy.john.corenpc.JohnNpc;
import me.shufy.john.events.JohnEvent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import static me.shufy.john.util.JohnUtility.bold;
import static me.shufy.john.util.JohnUtility.randomLocationNearPlayer;

public class MlgEvent extends JohnEvent {

    Collection<Player> deathList;

    public MlgEvent(World eventWorld, int duration, double chance) {
        super(eventWorld, "MlgEvent", bold(ChatColor.BLUE) + "MLG EVENT", "Land the MLG water bucket or face harsh consequences.", duration, chance);
    }

    @Override
    public void onEventCountdownStart() {
        deathList = new ArrayList<>();
        getPlayers().forEach(player ->  {
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(20);
            player.setFoodLevel(20);
            player.sendMessage(bold(ChatColor.YELLOW) + "Even if you die before the event even starts, you LOSE!");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1f, ThreadLocalRandom.current().nextFloat());
        });
    }

    @Override
    public void onEventStart() {
        getPlayers().forEach(player -> {
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
        });
    }

    @Override
    public void everyEventTick() {
        getPlayers().forEach(player -> {
            // ANTI CHEAT! :)
            if (getEventWorld().getHighestBlockAt(player.getLocation()).isLiquid()) {
                getEventWorld().getHighestBlockAt(player.getLocation()).setType(Material.BEDROCK);
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
            if (deathList.contains(player)) {
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
                            npc.targetTask.cancel();
                            npc.destroy();
                            this.cancel();
                        }
                        ticks++;
                    }
                }.runTaskTimer(plugin, 0, 1L);
            }
            player.getInventory().remove(Material.BUCKET);
            player.getInventory().remove(Material.WATER_BUCKET);
            player.getInventory().remove(Material.LAVA_BUCKET);
        });
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
