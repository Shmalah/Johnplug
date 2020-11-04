package me.shufy.john.randomevents.npc.events;

import me.shufy.john.randomevents.JohnableRandomEvent;
import me.shufy.john.randomevents.RandomEvent;
import me.shufy.john.randomevents.npc.Npc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import static me.shufy.john.util.JohnUtility.*;

public class JohnNpcChase extends RandomEvent implements JohnableRandomEvent {
    public JohnNpcChase(String eventName, Collection<Player> players, Location location, double chance) {
        super(eventName, players, location, chance);
    }
    Npc john;
    boolean shouldNotExecute = false;
    @Override
    public void setup(Location location) {
      //  Bukkit.getLogger().log(Level.INFO, "Running JohnNpcChase");
        if (location.getWorld().getEnvironment() != null)
        shouldNotExecute = (location.getWorld().getEnvironment().equals(World.Environment.NETHER));
        if (!shouldNotExecute) {
            john = new Npc("John", JOHN_UUID.toString(), location);
            setRunForTicks(randomInt(200, 500));
            setEventWorld(getWorldWithMostPlayers());
            setPlayers(getEventWorld().getPlayers());
            setLocation(randomPlayer(getEventWorld()).getEyeLocation());
        }
    }

    @Override
    public void execution(Collection<Player> playersInvolved) {
        if (!shouldNotExecute) {
            john.summon();
            if (john == null) {
                Bukkit.getLogger().log(Level.SEVERE, "[JohnNpcChase] JOHN IS NULL! Cannot execute the JohnableRandomEvent \"JohnNpcChase\"");
                Bukkit.getLogger().log(Level.SEVERE, "[JohnNpcChase] Event must halt.");
                return;
            }
            for (Player player : playersInvolved) {
                player.playSound(player.getLocation(), randomSoundWhoContains("ambient"), 1f, ThreadLocalRandom.current().nextFloat());
            }
        }
    }

    @Override
    public void cleanup(Location location, Collection<Player> playersInvolved) {
        if (john != null)
            john.destroy(false);
    }
}
