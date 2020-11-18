package me.shufy.john.survival.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static me.shufy.john.util.JohnUtility.*;

public class JohnItem {
    String itemName;
    ItemStack itemStack;
    Material type;
    Player owner;
    ItemAbility itemAbility;

    public enum ItemAbility {
        NONE, TRAMPOLINE, AURA, LUCK, LAZER
    }

    public JohnItem(String itemName) {
        this.itemName = itemName;
        this.itemStack = new ItemStack(randomMaterial());
        this.type = this.itemStack.getType();
        makeJohnItem(this.itemStack, this.itemName);
     //   JohnItemListener.getJohnItems().add(this); // only used for debug purposes. never use for tracking items across server instances or across /reload
    }
    public static ItemAbility getItemAbility(ItemStack itemStack) {
        return ItemAbility.valueOf(getLoreEntry(itemStack, 3).replace(ChatColor.RED.toString(), "").trim());
    }
    public static String randomJohnItemName() {
        StringBuilder strb = new StringBuilder();
        String[] c1 = {
            "Hayasaka's", "Jaden's", "John's", "JJ's", "Batman's", "The Stupid", "Isabelle's", "The Remarkable", "The Superior", "The Testicular", "The Fucked-Up", "The John", "The Semen", "The Cum", "The Ass", "The Super", "The Ultra", "The Indeterminate",
                "Austin's", "The Austin", "The Jaden", "The Garfield", "The Persona 5", "The Sonic", "The Mario", "The Princess Peach", "The Toadstool", "The Grant"
        };
        String[] c2 = {
            "Cum Stain", "Ass Wipe", "Bowling Ball", "Golf Ball", "Golden Obelisk", "Obelisk", "Sound-Proof", "Window", "Water Bottle", "Hydro Vesticular", "Intestination", "Sakuya", "Izayoi", "Flower", "Pumped", "Super-Magic", "Super-Duper",
                "Holocaust", "Hambergr", "Sticky", "Ranch-ey", "Fart-ey", "Poopy", "Jaden-ey", "Shufy-ey", "Rainbowzz", "ExplodingTNT", "Samuel", "Church's", "Mongrelium"
        };
        String[] c3 = {
            "Blower", "Pouncer", "Pounder", "Fucker", "Machine", "Machinery", "Objectoid", "Sakuya-oid", "Plower", "Gasser", "Girl Machine", "Slavery", "Sexer", "Server", "Observer", "Parser", "Plumber", "Pleasurer", "Humper", "Big Boy Machine",
                "Creamer", "Sucker", "Ribbler"
        };
        strb.append(c1[randomInt(c1.length)]).append(" ").append(c2[randomInt(c2.length)]).append(" ").append(c3[randomInt(c3.length)]);
        return strb.toString();
    }
    public static Material randomMaterial() {
        Material randMat = Material.values()[randomInt(Material.values().length)];
        while (!randMat.isItem()) randMat = Material.values()[randomInt(Material.values().length)];
        return randMat;
    }
    public static Material randomMaterialWhichContains(String contains) {
        contains = contains.toLowerCase();
        String finalContains = contains;
        List<Material> materials = Arrays.stream(Material.values()).filter(m -> m.name().toLowerCase().contains(finalContains)).collect(Collectors.toList());
        Bukkit.getLogger().log(Level.INFO, materials.stream().skip(randomInt(materials.size())).findFirst().get() + " is the item that john blessed");
        return materials.stream().skip(randomInt(materials.size())).findFirst().get();
    }
    public static ItemStack makeJohnItem(ItemStack itemStack, String itemName) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(bold(ChatColor.RED) + itemName);
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS);
        ItemAbility randomItemAbility = ItemAbility.values()[randomInt(ItemAbility.values().length)];
        itemMeta.setLore(new ArrayList<String>() {{
            add(ChatColor.DARK_GRAY + itemStack.getType().name().replace("_", " "));
            add(bold(ChatColor.GOLD) + "-=-=-=-=- John Item -=-=-=-=-");
            add(ChatColor.LIGHT_PURPLE + "Ability:");
            add(ChatColor.RED + randomItemAbility.name());
        }});
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public String toString() {
        try {
            return owner.getName() + "'s " + itemName;
        } catch (NullPointerException ex) {
            return itemName;
        }
    }

    public String getItemName() {
        return itemName;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Material getType() {
        return type;
    }

    public Player getOwner() {
        return owner;
    }
}
