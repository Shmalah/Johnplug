package me.shufy.john.randomevents.npc.events;

import me.shufy.john.randomevents.JohnableRandomEvent;
import me.shufy.john.randomevents.RandomEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

import static me.shufy.john.util.JohnUtility.*;

public class RandomNpcAppearance extends RandomEvent implements JohnableRandomEvent {

    public RandomNpcAppearance(String eventName, Collection<Player> players, Location location, double chance) {
        super(eventName, players, location, chance);
    }

    @Override
    public void setup(Location location) {
        setRunForTicks(randomInt(30, 60));
        setEventWorld(getWorldWithMostPlayers());
        setPlayers(getEventWorld().getPlayers());
        setLocation(randomPlayer(getEventWorld()).getEyeLocation());
    }

    @Override
    public void execution(Collection<Player> playersInvolved) {

    }

    @Override
    public void cleanup(Location location, Collection<Player> playersInvolved) {

    }

}
