package me.shufy.john.util.structs;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class PlayerPair {
    public Player first, second;
    public PlayerPair(Player first, Player second) {
        if (first == null || second == null)
            throw new IllegalArgumentException(String.format("Can't create the PlayerPair when one or both are null. First null? %s, Second null? %s", (first == null), (second == null)));
        this.first = first;
        this.second = second;
    }
    public PlayerPair(World world) {
        if (world == null)
            throw new IllegalArgumentException(String.format("Can't create a PlayerPair in a null world."));
        if (world.getPlayers().size() < 2)
            throw new IllegalStateException(String.format("Can't create a PlayerPair in world \"%s\" when there aren't at least 2 players in the world.", world.getName()));
        Collection<Player> worldPlayers = new ArrayList<Player>(world.getPlayers());
        try {
            this.first = worldPlayers.stream().skip(ThreadLocalRandom.current().nextInt(worldPlayers.size())).findFirst().orElse(null);
            this.second = worldPlayers.stream().filter(p -> !p.equals(this.first)).skip(ThreadLocalRandom.current().nextInt(worldPlayers.size()-1)).findFirst().orElse(null);
            if (this.first == null || this.second == null)
                throw new IllegalStateException("Tried to create a random player pair in world \"" + world.getName() + "\" but " + (first == null ? "first was null" : "second was null"));
            else if (this.first.equals(this.second))
                Bukkit.getLogger().log(Level.SEVERE, "Something went terribly wrong when creating a player pair. First player is identical to second.");
        } catch (Exception ex) {
            if (ex instanceof IndexOutOfBoundsException) {
                Bukkit.getLogger().log(Level.SEVERE, "Tried picking a random index in world players list that was beyond the world players list size (" + ex.getMessage() + ")");
            } else {
                Bukkit.getLogger().log(Level.WARNING, "An error occurred when picking two random players for PlayerPair: " + ex.getMessage());
            }
        }
    }
}
