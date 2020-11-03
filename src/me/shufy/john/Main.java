package me.shufy.john;

import me.shufy.john.biomes.JohnBiomeListener;
import me.shufy.john.items.JohnItemListener;
import me.shufy.john.randomevents.JohnableRandomEvent;
import me.shufy.john.randomevents.RandomEventListener;
import me.shufy.john.randomevents.appear.AnimalAppear;
import me.shufy.john.randomevents.npc.NpcJoinListen;
import me.shufy.john.randomevents.npc.events.RandomNpcAppearance;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new JohnBiomeListener(), this);
        getServer().getPluginManager().registerEvents(new JohnItemListener(), this);
        getServer().getPluginManager().registerEvents(new RandomEventListener(), this);
        getServer().getPluginManager().registerEvents(new NpcJoinListen(), this);
        AnimalAppear animalAppear = new AnimalAppear(); // starts random animal appear event

        // random johnable events
        JohnableRandomEvent johnNpcAppear = new RandomNpcAppearance("john npc appear", null, null, 0.05d);

        getCommand("jdb").setExecutor(new DebugCommands());
        getLogger().log(Level.WARNING, "The John plugin has been initialized!");
        if (!getServer().getOnlinePlayers().isEmpty()) {
            getServer().getOnlinePlayers().forEach(player -> {
                JohnItemListener.abilityCooldowns.putIfAbsent(player, player.getGameMode().equals(GameMode.CREATIVE) ? 0 : 11);
            });
        }
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.WARNING, "The John plugin has been disabled!");
    }
}
