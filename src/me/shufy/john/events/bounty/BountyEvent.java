package me.shufy.john.events.bounty;

import me.shufy.john.events.JohnEvent;
import me.shufy.john.util.JohnUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

import static me.shufy.john.util.JohnUtility.bold;
import static me.shufy.john.util.JohnUtility.randomInt;

public class BountyEvent extends JohnEvent {

    Player target;
    Player hunter;

    public BountyEvent(World eventWorld, int duration, double chance) {
        super(eventWorld, "BountyEvent", bold(ChatColor.DARK_RED) + "BOUNTY EVENT", "Kill the specified target", duration, chance);
        this.setEventStartCountdown(false);
    }

    @Override
    public void onEventCountdownStart() {

    }

    @Override
    public void onEventStart() {
        if (getPlayers().isEmpty() || Bukkit.getOnlinePlayers().isEmpty())
            return;
        Collection<Player> possiblePlayers = new ArrayList<>(getPlayers());
        hunter = possiblePlayers.stream().skip(randomInt(possiblePlayers.size())).findFirst().orElse(null);
        possiblePlayers.remove(hunter);
        target = possiblePlayers.stream().skip(randomInt(possiblePlayers.size())).findFirst().orElse(null);
        hunter.sendTitle(bold(ChatColor.DARK_RED) + "Kill " + bold(ChatColor.GOLD) + target.getName(), "Remember: It might not be worth it.", 100, 60, 100);
        hunter.playSound(hunter.getLocation(), JohnUtility.randomSoundWhoContains("MOOD"), 0.6f, 1f);
    }

    @Override
    public void everyEventTick() {

    }

    @Override
    public void onCountdownAnnounce(int secondsLeft) {

    }

    @Override
    public void onEventEndCountdownStart() {

    }

    @Override
    public void onEventEnd() {

    }
}
