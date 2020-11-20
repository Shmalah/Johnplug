package me.shufy.john.general.item;

import org.bukkit.inventory.ItemStack;

public abstract class GeneralJohnItem {
    ItemStack item;
    public GeneralJohnItem() {
        item = getItemStack();
    }
    public abstract ItemStack getItemStack();
    public abstract boolean equals(ItemStack other);
}
