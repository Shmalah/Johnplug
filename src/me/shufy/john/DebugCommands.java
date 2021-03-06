package me.shufy.john;

import me.shufy.john.corenpc.JohnNpc;
import me.shufy.john.corenpc.chase.NewJohnChase;
import me.shufy.john.survival.items.stupid.boat.JohnBoat;
import me.shufy.john.util.world.ParticleRay;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DebugCommands implements CommandExecutor {

    public static boolean debugMode = false;

    JohnNpc john;

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
                            john = new JohnNpc(player.getLocation());
                            john.spawn(player.getLocation().getWorld().getPlayers());
                            john.autoTarget();
                            break;
                        case "johnstop":
                            if (john != null)
                                john.stopAutoTarget();
                            break;
                        case "johndestroy":
                            if (john != null)
                                john.destroy();
                            break;
                        case "johncontinue":
                            if (john != null)
                                john.autoTarget();
                            break;
                        case "chance":
                            if (args.length > 1) {
                                try {
                                    NewJohnChase.chance = Double.parseDouble(args[1]);
                                    player.sendMessage("Chance set to " + (int)(Math.round(NewJohnChase.chance*100)) + "%");
                                } catch (Exception ex) {
                                    player.sendMessage("Invalid arg for /jdb chance. Usage: /jdb change <[double] percent chance>");
                                }
                            }
                            break;
                        case "newjohn":

                            break;
                        case "event":
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
