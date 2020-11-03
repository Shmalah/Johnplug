package me.shufy.john.randomevents.npc;

import org.bukkit.entity.Player;

// just some ideas on what I think an npc would implement, may be depreciated later on

public interface Npcable {
    void whenInPlayerRange(Player player); // player range = 3 blocks
    void whenInPlayerWorld(Player player);
    void playerIsClosest(Player player);
}
