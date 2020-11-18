package me.shufy.john.survival;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.Arrays;

public class SurvivalEventHandler implements Listener {
    @EventHandler
    public void craftItem (CraftItemEvent e) {
        Player player = (CraftPlayer)e.getWhoClicked();
        player.sendMessage(Arrays.toString(e.getInventory().getMatrix()));
    }
    private void customJohnItemTest() {

    }
}
