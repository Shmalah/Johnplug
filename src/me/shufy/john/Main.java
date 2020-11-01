package me.shufy.john;

import me.shufy.john.biomes.JohnBiomeListener;
import me.shufy.john.items.JohnItemListener;
import me.shufy.john.randomevents.RandomEventListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new JohnBiomeListener(), this);
        getServer().getPluginManager().registerEvents(new JohnItemListener(), this);
        getServer().getPluginManager().registerEvents(new RandomEventListener(), this);
        getCommand("jdb").setExecutor(new DebugCommands());
        getLogger().log(Level.WARNING, "The John plugin has been initialized!");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.WARNING, "The John plugin has been disabled!");
    }
}
