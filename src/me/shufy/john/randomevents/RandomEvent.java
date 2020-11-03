package me.shufy.john.randomevents;

import me.shufy.john.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

import static me.shufy.john.util.JohnUtility.*;

public class RandomEvent {

    // any sub-class of random event must implement johnablerandomevent or it wont even run

    String eventName;
    public static final Main plugin = Main.getPlugin(Main.class);
    BukkitTask eventTimer;

    private Collection<Player> players;

    World eventWorld;
    private Location location;
    private int runForTicks = -1;
    double chance;
    static boolean eventRunning;

    public RandomEvent(String eventName, Collection<Player> players, Location location, double chance) {
        this.eventName = eventName;
        this.players = players;
        this.location = location;
        this.chance = chance;
        Bukkit.getLogger().log(Level.INFO, "RandomEvent \"" + eventName + "\" has been constructed.");
        eventRunnable(); // start coin throw/event loop
    }

    private void eventRunnable() {
        this.eventTimer = new BukkitRunnable() {
            @Override
            public void run() {
                if (randomChance(chance)) {
                    if (!eventRunning) {
                        if (!Bukkit.getOnlinePlayers().isEmpty()) { // there has to be players online to actually do the event DINGUS
                            eventRunning = true;
                            if (location == null)
                                location = randomPlayer(Bukkit.getWorlds().get(0)).getLocation(); // random player in default world (there may be none)
                            setup(location);
                            if (runForTicks == -1)
                                setRunForTicks(60); // 60 ticks is the default event run time, so set it if the user hasn't overridden it
                            if (players == null)
                                players = new ArrayList<>(Bukkit.getOnlinePlayers()); // default is ALL PLAYERS in the entire server
                            if (eventWorld == null);
                                eventWorld = location.getWorld(); // set world of location
                            execution(players);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    cleanup(location, players);
                                    eventRunning = false;
                                }
                            }.runTaskLater(plugin, runForTicks);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20L); // every second there's an x percent chance to run (IF IT ISNT ALREADY RUNNING)
    }

    // interface methods that you should override in your sub-class of randomevent

    public void setup(Location location) {

    }

    public void execution(Collection<Player> playersInvolved) {

    }

    public void cleanup(Location location, Collection<Player> playersInvolved) {

    }

    public String getEventName() {
        return eventName;
    }

    public BukkitTask getEventTimer() {
        return eventTimer;
    }

    public double getChance() {
        return chance;
    }

    public static boolean isEventRunning() {
        return eventRunning;
    }

    public int getRunForTicks() {
        return runForTicks;
    }

    public void setRunForTicks(int runForTicks) {
        this.runForTicks = runForTicks;
    }

    public static Main getPlugin() {
        return plugin;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public Location getLocation() {
        return location;
    }

    public void setPlayers(Collection<Player> players) {
        this.players = players;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public void setEventWorld(World eventWorld) {
        this.eventWorld = eventWorld;
    }

    public World getEventWorld() {
        return eventWorld;
    }

}
