package me.shufy.john.events;


import me.shufy.john.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import static me.shufy.john.util.JohnUtility.bold;

public abstract class JohnEvent {

    public static final Main plugin = Main.getPlugin(Main.class);

    String name;
    String displayName;
    String eventDescription;

    World eventWorld;
    Collection<Player> players;

    double chance;
    int duration;

    boolean ignoreChance = false;
    boolean eventRunning = false;
    boolean eventStartCountdown = true;
    boolean eventEndCountdown = true;

    public JohnEvent(World eventWorld, String name, String displayName, String eventDescription, int duration, double chance) {
        players = new ArrayList<>(eventWorld.getPlayers());
        if (players.isEmpty())
            return;
        this.displayName = displayName;
        this.duration = duration;
        this.chance = chance;
        this.displayName = bold(ChatColor.GOLD) + name;
        this.eventWorld = eventWorld;
        this.eventDescription = eventDescription;
        this.name = name;
        chanceRunner();
    }

    private void chanceRunner() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ThreadLocalRandom.current().nextDouble() < chance || isIgnoreChance()) {
                    if (!eventRunning)
                        runEvent();
                }
            }
        }.runTaskTimer(plugin, 0, (20L * 10));
    }

    private void runEvent() {
        eventRunning = true;
        if (isEventStartCountdown()) {
            onEventCountdownStart();
            new BukkitRunnable() {
                private int secondsLeft = 10;
                @Override
                public void run() {
                    if (secondsLeft == 1)
                        this.cancel();
                    for (Player player : getPlayers()) {
                        if (secondsLeft > 7) {
                            player.sendTitle(displayName, secondsLeft + " seconds until event starts", 10, 60, 10);
                        } else {
                            player.sendTitle(bold(ChatColor.RED) + secondsLeft + " seconds until event starts", eventDescription, 10, 60, 10);
                        }
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f);
                    }
                    secondsLeft--;
                }
            }.runTaskTimerAsynchronously(plugin, 0, 20L);
        }
        onEventStart();
        startEventTick();
    }

    // TODO finish john event class

    private void startEventTick() {
        new BukkitRunnable() {
            private int ticks = 0;
            @Override
            public void run() {
                everyEventTick();
                ticks++;
                if (ticks % 20 == 0) {
                    duration--;
                }
                if ((duration % 15 == 0 && duration != 0) || duration <= 10 && duration > 0) {
                    if (duration <= 10)
                        onEventEndCountdownStart();
                } else if (duration == 0) {
                    onEventEnd();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1L);
    }

    private void endEvent() {
        eventRunning = false;
    }

    public abstract void onEventCountdownStart();
    public abstract void onEventStart();
    public abstract void everyEventTick();
    public abstract void onEventEndCountdownStart();
    public abstract void onEventEnd();

    public static Main getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public World getEventWorld() {
        return eventWorld;
    }

    public void setEventWorld(World eventWorld) {
        this.eventWorld = eventWorld;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Collection<Player> players) {
        this.players = players;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public boolean isEventRunning() {
        return eventRunning;
    }

    public void setEventRunning(boolean eventRunning) {
        this.eventRunning = eventRunning;
    }

    public boolean isEventStartCountdown() {
        return eventStartCountdown;
    }

    public void setEventStartCountdown(boolean eventStartCountdown) {
        this.eventStartCountdown = eventStartCountdown;
    }

    public boolean isEventEndCountdown() {
        return eventEndCountdown;
    }

    public void setEventEndCountdown(boolean eventEndCountdown) {
        this.eventEndCountdown = eventEndCountdown;
    }

    public boolean isIgnoreChance() {
        return ignoreChance;
    }

    public void setIgnoreChance(boolean ignoreChance) {
        this.ignoreChance = ignoreChance;
    }

    public int getDuration() {
        return duration;
    }
}
