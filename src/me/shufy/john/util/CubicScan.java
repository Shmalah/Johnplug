package me.shufy.john.util;

import me.shufy.john.items.JohnItem;
import me.shufy.john.items.JohnItemListener;
import me.shufy.john.items.weapons.JohnItemAbilities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

@Deprecated
public class CubicScan {
    int blocks, area;
    Location origin, blockFound = null;
    Material materialToFind;
    Set<Block> scannedBlocks = new HashSet<>();
    public CubicScan(Location origin, int blocks, Material materialToFind) {
        this.origin = origin;
        origin.setY(0);
        this.blocks = blocks;
        this.area = (int) Math.round(Math.pow(blocks, 3));
        this.materialToFind = materialToFind;
    }
    public Location result() {
        new BukkitRunnable() {
            int ticks = 1;
            @Override
            public void run() {
                if (ticks == 1) loop();
                ticks++;
            }
            public void loop() {
                for (int x = 0; x < blocks; x++) {
                    if (ticks % 20 != 0) continue;
                    for (int z = 0; z < blocks; z++) {
                        if (ticks % 20 != 0) continue;
                        for (int y = 0; y < origin.getWorld().getHighestBlockYAt(x, z); y++) {
                            if (ticks % 20 != 0) continue;
                            Bukkit.getLogger().log(Level.INFO, String.format("X: %s, Y: %s, Z: %s", x, y, z));
                            if (origin.getBlock().getRelative(x, y, z).getType().equals(materialToFind)) {
                                blockFound = origin.getBlock().getRelative(x, y, z).getLocation();
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(JohnItemAbilities.plugin, 0, 1L);
        return blockFound;
    }
}
