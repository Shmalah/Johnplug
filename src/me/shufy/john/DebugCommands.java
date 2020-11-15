package me.shufy.john;

import me.shufy.john.corenpc.JohnNpc;
import me.shufy.john.items.stupid.boat.JohnBoat;
import me.shufy.john.util.ParticleRay;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class DebugCommands implements CommandExecutor {

    public static boolean debugMode = false;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (s.equalsIgnoreCase("jdb")) {
                if (args.length > 0) {
                    switch (args[0]) {
                        case "debug":
                        case "debugmode":
                            debugMode = !debugMode;
                            player.sendMessage("Debug mode is now: " + debugMode);
                            break;
                        case "johnboat":
                            player.getInventory().addItem(JohnBoat.johnBoat());
                            break;
                        case "johnjohn":
                            JohnNpc john = new JohnNpc(player.getLocation());
                            john.spawn(player.getLocation().getWorld().getPlayers());
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Vector vDelta = player.getLocation().toVector().subtract(john.getNpc().getBukkitEntity().getLocation().toVector()).normalize().multiply(0.3d);
                                    john.look(player.getEyeLocation()); // look into the eyes of your victim.
                                    if (john.getNpc().getBukkitEntity().getLocation().getBlock().isLiquid())
                                        vDelta.multiply(0.5);
                                    john.move(vDelta.getX(), vDelta.getY(), vDelta.getZ()); // move toward your victim, john. It's time.
                                }
                            }.runTaskTimer(Main.getPlugin(Main.class), 20L, 1L);
                            break;
                        case "particleray":
                            ParticleRay particleRay = new ParticleRay(((Player) commandSender).getEyeLocation(), player.getEyeLocation().getDirection(), 20, Color.RED, 3);
                            new BukkitRunnable() {
                                int ticks = 0;
                                @Override
                                public void run() {
                                    if (ticks >= 200) this.cancel();
                                    particleRay.draw();
                                    ticks++;
                                }
                            }.runTaskTimer(Main.getPlugin(Main.class), 0, 5L);
                            break;
                        default:
                            player.sendMessage(String.format("%s\"%s\" is not a valid argument for /jdb", ChatColor.RED, args[0]));
                            break;
                    }
                        return false;
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: /jdb <debug option>");
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }
}
