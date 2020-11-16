package me.shufy.john.items.egg;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;

import java.util.concurrent.ThreadLocalRandom;

public class JohnEggListener implements Listener {

    @EventHandler
    public void eggLaunch (PlayerEggThrowEvent e) {

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
