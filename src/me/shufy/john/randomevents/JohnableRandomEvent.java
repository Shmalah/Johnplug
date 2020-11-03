package me.shufy.john.randomevents;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface JohnableRandomEvent {
    void setup(Location location);
    void execution(Collection<Player> playersInvolved);
    void cleanup(Location location, Collection<Player> playersInvolved);
}
