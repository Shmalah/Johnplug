package me.shufy.john.survival.items.egg;

import me.shufy.john.util.JohnUtility;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

import static me.shufy.john.util.JohnUtility.randomInt;

public class JohnEggListener implements Listener {

    @EventHandler
    public void eggLaunch (ProjectileLaunchEvent e) {
        if (e.getEntityType().equals(EntityType.EGG)) {
            Egg egg = (Egg)e.getEntity();
            if (JohnUtility.loreContains(egg.getItem(), "John's lovely egg.")) {
                Player shooter = (Player) JohnUtility.getClosestEntity(egg.getLocation());
                egg.setBounce(true);
                egg.setFireTicks(60);
                egg.setVelocity(shooter.getLocation().getDirection().multiply(3));
            }
        }
    }

    @EventHandler
    public void eggProjectileLand (ProjectileHitEvent e) {
        // regular projectile hit event
        if (e.getEntity().getType() == EntityType.EGG) {
            Egg egg = (Egg)e.getEntity();
            ItemStack eggItem = egg.getItem();
            if (JohnUtility.loreContains(eggItem, "John's lovely egg.")) {
                // this is the john egg landing
                if (e.getHitEntity() != null) {
                    // it hit an entity
                    e.getHitEntity().setFireTicks(60);
                    e.getHitEntity().setVelocity(egg.getLocation().getDirection());
                    e.getHitEntity().getWorld().createExplosion(e.getHitEntity().getLocation(), 1f);
                    if (e.getHitEntity() instanceof LivingEntity) {
                        LivingEntity hitEntity = (LivingEntity) e.getHitEntity();
                        hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, randomInt(200, 600), 4));
                        hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, randomInt(200, 600), 1));
                        hitEntity.setNoDamageTicks(0);
                        if (hitEntity instanceof Player) {
                            Player hitPlayer = (Player)hitEntity;
                            hitPlayer.setFireTicks(60);
                            double finalDamage = 20.0;
                            if (hitPlayer.getInventory().getArmorContents().length > 0) {
                                for (ItemStack armorContent : hitPlayer.getInventory().getArmorContents()) {
                                    if (armorContent.getType().name().contains("NETHERITE")) {
                                        if (finalDamage - 4 > 0)
                                            finalDamage -= 4;
                                    } else if (armorContent.getType().name().contains("DIAMOND")) {
                                        if (finalDamage - 2 > 0)
                                            finalDamage -= 2;
                                    }
                                }
                            }
                            hitPlayer.damage(finalDamage);
                        }
                    }
                } else if (e.getHitBlock() != null) {
                    e.getHitBlock().getLocation().getWorld().strikeLightningEffect(e.getHitBlock().getLocation());
                }
            }
        }
    }

    @EventHandler
    public void eggEgg (PlayerEggThrowEvent e) {
        if (JohnUtility.loreContains(e.getEgg().getItem(), "John's lovely egg.")) {
            e.setHatching(false); // dont hatch any chickens if it's a john egg
        }
    }

    @EventHandler
    public void obtain (EntityDeathEvent e) {
        // chickens drop the john egg!
        if (e.getEntityType().equals(EntityType.CHICKEN)) {
            if (ThreadLocalRandom.current().nextDouble() < 0.05d) { // 5% chance
                JohnEgg johnEgg = new JohnEgg();
                e.getDrops().add(johnEgg.getItemStack());
            }
        }
    }
}
