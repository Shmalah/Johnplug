package me.shufy.john.items;

import org.bukkit.entity.Player;

public interface JohnableItem {
    void onLeftClickBlock(JohnItem.ItemAbility itemAbility, Player player);
    void onLeftClickAir(JohnItem.ItemAbility itemAbility, Player player);
    void onRightClickBlock(JohnItem.ItemAbility itemAbility, Player player);
    void onRightClickAir(JohnItem.ItemAbility itemAbility, Player player);
}
