package me.shufy.john.survival.items;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public interface JohnableItem {
    void onLeftClickBlock(JohnItem.ItemAbility itemAbility, Player player, PlayerInteractEvent playerInteractEvent);
    void onLeftClickAir(JohnItem.ItemAbility itemAbility, Player player, PlayerInteractEvent playerInteractEvent);
    void onRightClickBlock(JohnItem.ItemAbility itemAbility, Player player, PlayerInteractEvent playerInteractEvent);
    void onRightClickAir(JohnItem.ItemAbility itemAbility, Player player, PlayerInteractEvent playerInteractEvent);
}
