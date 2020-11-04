package me.shufy.john.randomevents.challenges;

import me.shufy.john.Main;
import me.shufy.john.randomevents.npc.JohnChallenge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

import static me.shufy.john.util.JohnUtility.bold;
import static me.shufy.john.util.JohnUtility.randomChance;

public class Challenge implements JohnChallenge {

    public static final Main plugin = Main.getPlugin(Main.class);
    static boolean challengeAlreadyRunning = false;

    int eventDuration = 60; // default
    String challengeName;
    Collection<Player> players;

    public Challenge(String challengeName) {
        this.challengeName = challengeName;
        this.players = new ArrayList<>(Bukkit.getOnlinePlayers()); // by default everyone in the server gets the challenge. This can be overriden using setPlayersList
        challengeRunner().runTaskTimer(plugin, 0, 1L);
    }

    private BukkitRunnable challengeRunner() {
        return new BukkitRunnable() {
            private int ticks = 0;
            @Override
            public void run() {
                if (canStartChallenge()) start();
                nextTick();
            }
            private boolean canStartChallenge() {
                return ticksMultipleOf(60) && randomChance(0.10d) && !isChallengeAlreadyRunning();
            }
            private boolean ticksMultipleOf(int x) {
                return getCurrentTick() % x == 0;
            }
            private int getCurrentTick() {
                return ticks;
            }
            private void nextTick() {
                ticks++;
            }
        };
    }

    public void start() {
        if (!isChallengeAlreadyRunning()) {
            challengeAlreadyRunning = true;
            onChallengeCountdownStart();
            challengeCountdown();
        } else {
            Bukkit.getLogger().log(Level.WARNING, String.format("Tried to start an event \"%s\" when it's already running ...", this.challengeName));
        }
    }

    public void stop() {
        if (isChallengeAlreadyRunning()) {
            challengeAlreadyRunning = false;
        } else {
            Bukkit.getLogger().log(Level.WARNING, String.format("Tried to stop an event \"%s\" when it's not running ...", this.challengeName));
        }
    }

    // TODO create challenge class

    private void challengeCountdown() {
        new BukkitRunnable() {
            private int secondsLeft = 10;
            @Override
            public void run() {
                countdown();
            }
            private void countdown() {
                if (secondsLeft > 0) {
                    secondsLeft--;
                } else {
                    challengeTick();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20L);
    }

    private void challengeTick() {
        new BukkitRunnable() {
            private int ticks = 0;
            private int secondsLeft = getEventDuration();
            @Override
            public void run() {
                eventEndCountdown();
                nextTick();
            }
            private void nextTick() {
                ticks++;
                onChallengeTick(ticks);
            }
            private void eventEndCountdown() {
                if (ticks % 20 == 0 && ticks != 0)  // every second
                    secondsLeft--;
                if ((secondsLeft % 15 == 0 && secondsLeft != 0) || secondsLeft <= 10)
                    onCountdownAnnounce(secondsLeft);
                if (secondsLeft == 0) {
                    onChallengeEnd();
                    stop();
                }
            }
        }.runTaskTimer(plugin, 0, 1L);
    }

    public void onChallengeCountdownStart() {

    }

    // default implementation
    public void onCountdownAnnounce(int secondsRemaining) {
        players.forEach(player -> {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.9f, 1f);
            player.sendMessage(String.format("%s %s %s seconds remaining", bold(ChatColor.GOLD), secondsRemaining, bold(ChatColor.RED)));
        });
    }

    // main event loop, must be implemented
    public void onChallengeTick(int ticks) {

    }

    public void onChallengeEnd() {

    }

    public static boolean isChallengeAlreadyRunning() {
        return challengeAlreadyRunning;
    }

    public void setPlayersList(Collection<Player> playersList) {
        this.players = playersList;
    }

    public int getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(int eventDuration) {
        this.eventDuration = eventDuration;
    }

}
