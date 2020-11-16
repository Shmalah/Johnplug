package me.shufy.john;

import me.shufy.john.corenpc.JohnListener;
import me.shufy.john.corenpc.JohnNpc;
import me.shufy.john.items.JohnItemListener;
import me.shufy.john.items.egg.JohnEggListener;
import me.shufy.john.items.johnrod.JohnRodListener;
import me.shufy.john.items.sand.JohnSandListener;
import me.shufy.john.items.stupid.boat.JohnBoatListener;
import me.shufy.john.scare.Npc;
import me.shufy.john.scare.Spooker;
import me.shufy.john.scare.SpookerStorm;
import org.bukkit.GameMode;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {

        //getServer().getPluginManager().registerEvents(new JohnBiomeListener(), this);
        getServer().getPluginManager().registerEvents(new JohnItemListener(), this);
        getServer().getPluginManager().registerEvents(new JohnBoatListener(), this);
        getServer().getPluginManager().registerEvents(new JohnListener(), this);

        // john items/blocks
        getServer().getPluginManager().registerEvents(new JohnSandListener(), this);
        getServer().getPluginManager().registerEvents(new JohnRodListener(), this);
        getServer().getPluginManager().registerEvents(new JohnEggListener(), this);

        // john
        Npc.initializeNpcs();
        new Spooker();
        new SpookerStorm();

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
        Npc.deinitializeNpcs();
        JohnNpc.deinitializeJohns();
    }
}
