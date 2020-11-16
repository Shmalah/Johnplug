package me.shufy.john.events.mlg;

import me.shufy.john.events.JohnEvent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static me.shufy.john.util.JohnUtility.bold;

public class MlgEvent extends JohnEvent {
    public MlgEvent(World eventWorld, int duration, double chance) {
        super(eventWorld, "MlgEvent", bold(ChatColor.BLUE) + "MLG EVENT", "Land the MLG water bucket or face harsh consequences.", duration, chance);
    }

    @Override
    public void onEventCountdownStart() {
        getPlayers().forEach(player -> player.setGameMode(GameMode.SURVIVAL));
    }

    @Override
    public void onEventStart() {
        getPlayers().forEach(player -> player.getInventory().addItem(mlgWaterBucket()));
    }

    @Override
    public void everyEventTick() {

    }

    @Override
    public void onCountdownAnnounce(int secondsLeft) {

    }

    @Override
    public void onEventEndCountdownStart() {

    }

    @Override
    public void onEventEnd() {

    }

    private ItemStack mlgWaterBucket() {
        ItemStack mlgBucket = new ItemStack(Material.WATER_BUCKET);
        ItemMeta mlgMeta = mlgBucket.getItemMeta();
        mlgMeta.setDisplayName(bold(ChatColor.BLUE) + "MLG WATER BUCKET");
        mlgMeta.setLore(new ArrayList<String>() {{
            add (ChatColor.GOLD + "The official MLG water bucket. Stop looking at this and land your mlg idiot");
        }});
        mlgMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        mlgBucket.setItemMeta(mlgMeta);
        return mlgBucket;
    }
}
