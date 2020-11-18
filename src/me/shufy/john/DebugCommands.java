package me.shufy.john;

import me.shufy.john.corenpc.JohnNpc;
import me.shufy.john.events.JohnEvent;
import me.shufy.john.events.bounty.BountyEvent;
import me.shufy.john.events.mlg.MlgEvent;
import me.shufy.john.survival.items.johnmask.JohnMask;
import me.shufy.john.survival.items.stupid.boat.JohnBoat;
import me.shufy.john.util.ParticleRay;
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
                        case "event":
                            BountyEvent bountyEvent = new BountyEvent(player.getWorld(), 0.1d);
                            bountyEvent.setIgnoreChance(true);
                            break;
                        case "mask":
                            JohnMask mask = new JohnMask();
                            player.getInventory().addItem(mask.getItemStack());
                            break;
                        case "eventdebug":
                            if (JohnEvent.isEventInstanceRunning()) {
                                if (JohnEvent.getEventInstance() instanceof MlgEvent) {
                                    MlgEvent mlgEvent1 = (MlgEvent) JohnEvent.getEventInstance();
                                    player.sendMessage(mlgEvent1.deathList.toString());
                                    player.sendMessage(mlgEvent1.winList.toString());
                                }
                            } else if (BountyEvent.isInstanceRunning()) {
                               BountyEvent runningBounty = BountyEvent.getRunningInstance();
                               if (runningBounty != null) {
                                   // debug placeholder for later
                                   player.sendMessage("Bounty Event World: " + runningBounty.getBountyEventWorld().getName());
                                   player.sendMessage("Hunter: " + runningBounty.getHunter().getName() + ", Bounty/Target: " + runningBounty.getTarget().getName());
                               }
                            }
                            break;
                        case "eventcollide":
                            for (int i = 0; i < 50; i++) {
                                BountyEvent b = new BountyEvent(player.getWorld(), 0.1d);
                                b.setIgnoreChance(true);
                                MlgEvent m = new MlgEvent(player.getWorld(), 20, 0.2d);
                                m.setIgnoreChance(true);
                            }
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
