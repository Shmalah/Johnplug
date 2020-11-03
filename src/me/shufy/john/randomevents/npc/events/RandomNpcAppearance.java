package me.shufy.john.randomevents.npc.events;

import me.shufy.john.randomevents.JohnableRandomEvent;
import me.shufy.john.randomevents.RandomEvent;
import me.shufy.john.randomevents.npc.Npc;
import me.shufy.john.randomevents.npc.NpcAbility;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import static me.shufy.john.util.JohnUtility.*;

public class RandomNpcAppearance extends RandomEvent implements JohnableRandomEvent {

    Npc john = new Npc("John", "4523497a-d9af-4b8d-ae8a-33400fdb92d6");

    public RandomNpcAppearance(String eventName, Collection<Player> players, Location location, double chance) {
        super(eventName, players, location, chance);
    }

    @Override
    public void setup(Location location) {
        setRunForTicks(randomInt(100, 150));
        setEventWorld(getWorldWithMostPlayers());
        setPlayers(getEventWorld().getPlayers());
        setLocation(randomPlayer(getEventWorld()).getEyeLocation());
    }

    @Override
    public void execution(Collection<Player> playersInvolved) {
        for (Player player : playersInvolved) {
            player.playSound(player.getLocation(), randomSoundWhoContains("mood"), 1f, ThreadLocalRandom.current().nextFloat());
        }
        john.addAbility(NpcAbility.FOLLOW);
        john.addAbility(NpcAbility.ATTACK);
        john.summon(getLocation());
    }

    @Override
    public void cleanup(Location location, Collection<Player> playersInvolved) {
        john.removeAllAbilities();
        john.destroy();
    }

}
