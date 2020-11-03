package me.shufy.john.randomevents.npc.events;

import me.shufy.john.randomevents.JohnableRandomEvent;
import me.shufy.john.randomevents.RandomEvent;
import me.shufy.john.randomevents.npc.Npc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import static me.shufy.john.util.JohnUtility.*;

public class RandomNpcAppearance extends RandomEvent implements JohnableRandomEvent {

    public RandomNpcAppearance(String eventName, Collection<Player> players, Location location, double chance) {
        super(eventName, players, location, chance);
    }

    Npc john;

    @Override
    public void setup(Location location) {
        john = new Npc("John", "4523497a-d9af-4b8d-ae8a-33400fdb92d6", location);
        setRunForTicks(randomInt(5, 15));
        setEventWorld(getWorldWithMostPlayers());
        setPlayers(getEventWorld().getPlayers());
        setLocation(randomPlayer(getEventWorld()).getEyeLocation());
    }

    @Override
    public void execution(Collection<Player> playersInvolved) {
        if (john == null) {
            Bukkit.getLogger().log(Level.SEVERE, "JOHN IS NULL! Cannot execute the JohnableRandomEvent \"RandomNpcAppearance\"");
            Bukkit.getLogger().log(Level.SEVERE, "Event must halt.");
            return;
        }
        for (Player player : playersInvolved) {
            player.playSound(player.getLocation(), randomSoundWhoContains("mood"), 1f, ThreadLocalRandom.current().nextFloat());
        }
    }

    @Override
    public void cleanup(Location location, Collection<Player> playersInvolved) {
        john.destroy();
    }

}
