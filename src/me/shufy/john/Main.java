package me.shufy.john;

import me.shufy.john.corenpc.JohnListener;
import me.shufy.john.corenpc.JohnNpc;
import me.shufy.john.events.bounty.BountyEvent;
import me.shufy.john.events.bounty.BountyEventListener;
import me.shufy.john.events.mlg.MlgEvent;
import me.shufy.john.events.mlg.MlgEventListener;
import me.shufy.john.player.BlockLogger;
import me.shufy.john.scare.Npc;
import me.shufy.john.scare.Spooker;
import me.shufy.john.scare.SpookerStorm;
import me.shufy.john.survival.SurvivalEventHandler;
import me.shufy.john.survival.crafting.recipes.tools.PowerAxe;
import me.shufy.john.survival.items.JohnItemListener;
import me.shufy.john.survival.items.egg.JohnEggListener;
import me.shufy.john.survival.items.johnmask.JohnMaskListener;
import me.shufy.john.survival.items.johnrod.JohnRodListener;
import me.shufy.john.survival.items.sand.JohnSandListener;
import me.shufy.john.survival.items.stupid.boat.JohnBoatListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

import static me.shufy.john.util.JohnUtility.bold;
import static me.shufy.john.util.JohnUtility.getWorldWithMostPlayers;

public class Main extends JavaPlugin {

    public static MlgEvent mlgEvent;
    public static BountyEvent bountyEvent;

    @Override
    public void onEnable() {

        // TODO make MLG event work globally and automatic
        // TODO make bounty event work globally and automatic

        //getServer().getPluginManager().registerEvents(new JohnBiomeListener(), this);
        getServer().getPluginManager().registerEvents(new JohnItemListener(), this);
        getServer().getPluginManager().registerEvents(new JohnBoatListener(), this);
        getServer().getPluginManager().registerEvents(new JohnListener(), this);

        // john items/blocks
        getServer().getPluginManager().registerEvents(new JohnSandListener(), this);
        getServer().getPluginManager().registerEvents(new JohnRodListener(), this);
        getServer().getPluginManager().registerEvents(new JohnEggListener(), this);
        getServer().getPluginManager().registerEvents(new JohnMaskListener(), this);

        // john
        Npc.initializeNpcs();
        new Spooker();
        new SpookerStorm();

        // john events
        getServer().getPluginManager().registerEvents(new MlgEventListener(), this);
        getServer().getPluginManager().registerEvents(new BountyEventListener(), this);

        // john survival / items too
        getServer().getPluginManager().registerEvents(new SurvivalEventHandler(), this);
        new PowerAxe();

        // player loggers
        getServer().getPluginManager().registerEvents(new BlockLogger(), this);

        getCommand("jdb").setExecutor(new DebugCommands());

        getLogger().log(Level.WARNING, "The John plugin has been initialized!");
        if (!getServer().getOnlinePlayers().isEmpty()) {
            getServer().getOnlinePlayers().forEach(player -> {
                JohnItemListener.abilityCooldowns.putIfAbsent(player, player.getGameMode().equals(GameMode.CREATIVE) ? 0 : 11);
            });
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(bold(ChatColor.RED) + "Using John's World v0.1.3");
        }

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            Bukkit.getLogger().log(Level.INFO, "Initializing bounty event");
            bountyEvent = new BountyEvent(getWorldWithMostPlayers(), 0.1d);
            Bukkit.getLogger().log(Level.INFO, "Initializing MLG event");
            mlgEvent = new MlgEvent(getWorldWithMostPlayers(), 15, 0.1d);
        }

    }
    @Override
    public void onDisable() {
        Npc.deinitializeNpcs();
        JohnNpc.deinitializeJohns();
    }
}
