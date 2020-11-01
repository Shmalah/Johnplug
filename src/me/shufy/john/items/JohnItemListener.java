package me.shufy.john.items;

import me.shufy.john.Main;
import me.shufy.john.items.weapons.JohnItemAbilities;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import static me.shufy.john.util.JohnUtility.*;

public class JohnItemListener implements Listener {

    @Deprecated
    /*
    *  do not rely on johnitems list for checking what items are actually present in the server. use johnitemlistener event handlers and COOLDOWNS instead
    */
    private static ArrayList<JohnItem> johnItems = new ArrayList<>();

    static HashMap<Player, Integer> abilityCooldowns = new HashMap<>();
    static BukkitTask cooldownsTimer;
    static JohnItemAbilities johnItemAbilities = new JohnItemAbilities();
    BukkitTask auraTask;

    public static final Main plugin = Main.getPlugin(Main.class);

    public JohnItemListener() {
        Bukkit.getOnlinePlayers().forEach(player -> abilityCooldowns.putIfAbsent(player, 11));
        cooldownsTimer = new BukkitRunnable() {
            @Override
            public void run() {
                abilityCooldowns.forEach((player, cooldown) -> {
                    if (cooldown > 0) {
                        abilityCooldowns.put(player, cooldown - 1);
                    }
                });
            }
        }.runTaskTimer(plugin, 0, 20L);
    }

    @EventHandler
    public void entityDeath (EntityDeathEvent e) {
        if (!(e instanceof Player) && e.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            if (randomChance(0.05d)) {
                e.getDrops().add(new JohnItem(JohnItem.randomJohnItemName()).getItemStack());
            }
        }
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        abilityCooldowns.putIfAbsent(e.getPlayer(), 11);
    }

    @EventHandler
    public void johnItemInteract (PlayerInteractEvent e) {

        Player player = e.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (itemHasLore(heldItem)) {
            //Bukkit.getLogger().log(Level.INFO, heldItem.getType().name() + " has lore :3");
            if (heldItem.getItemMeta().getLore().get(1).contains("John Item")) {

                Bukkit.getLogger().log(Level.INFO, heldItem.getItemMeta().getDisplayName() + " is a john item! Player's cooldown: " + abilityCooldowns.get(player));

                if (abilityCooldowns.get(player) != 0) {
                    player.sendMessage(bold(ChatColor.RED) + "Your john ability is on cooldown! " + abilityCooldowns.get(player) + " seconds left");
                    e.setCancelled(true);
                    return;
                }

                player.sendMessage(bold(ChatColor.GREEN) + "You used the John " + JohnItem.getItemAbility(heldItem) + " ability!");
                JohnItem.ItemAbility itemAbility = JohnItem.getItemAbility(heldItem);

                Bukkit.getLogger().log(Level.INFO, "Action: " + e.getAction() + ", Ability: " + itemAbility.name() + ", Player: " + player.getName());
                switch (e.getAction()) {
                    case LEFT_CLICK_BLOCK:
                        johnItemAbilities.onLeftClickBlock(itemAbility, player, e);
                        break;
                    case RIGHT_CLICK_BLOCK:
                        johnItemAbilities.onRightClickBlock(itemAbility, player, e);
                        break;
                    case LEFT_CLICK_AIR:
                        johnItemAbilities.onLeftClickAir(itemAbility, player, e);
                        break;
                    default:
                    case RIGHT_CLICK_AIR:
                        johnItemAbilities.onRightClickAir(itemAbility, player, e);
                        break;
                    case PHYSICAL:
                        break;
                }

                abilityCooldowns.put(player, 11);
            }
        }
    }

    @Deprecated
    /*
     *  do not rely on johnitems list for checking what items are actually present in the server. use johnitemlistener instead
     */
    public static ArrayList<JohnItem> getJohnItems() {
        return johnItems; // do not rely on johnitems list for checking what items are actually present in the server.
    }

}
