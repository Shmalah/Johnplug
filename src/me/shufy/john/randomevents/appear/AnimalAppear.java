package me.shufy.john.randomevents.appear;

import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

import static me.shufy.john.util.JohnUtility.*;

public class AnimalAppear extends Appear implements JohnAppearable {

    Animals randomAnimal = null;

    @Override
    public void beforeAppearance(Player appearTo) {
        appearTo.playSound(appearTo.getLocation(), randomSoundWhoContains("mood"),1f, ThreadLocalRandom.current().nextFloat());
    }

    @Override
    public void duringAppearance(Location appearLocation) {
        ticksToAppearFor = 300;
        appearLocation.setY(appearLocation.getWorld().getHighestBlockYAt(appearLocation.getBlockX(), appearLocation.getBlockZ()));
        randomAnimal = (Animals) appearLocation.getWorld().spawnEntity(appearLocation, randomAnimal());
        randomAnimal.setAI(false);
        randomAnimal.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2000, 200));
    }

    @Override
    public void afterAppearance(Location playerLocation) {
        if (randomAnimal != null)
            randomAnimal.remove();
        if (playerLocation == null) return; // player probably left the server?
        playerLocation.getWorld().playSound(playerLocation, randomSoundWhoContains("ender"), 0.5f, ThreadLocalRandom.current().nextFloat());
    }

}
