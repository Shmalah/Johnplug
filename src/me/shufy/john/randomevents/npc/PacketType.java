package me.shufy.john.randomevents.npc;

// NPC_PLAYER_JOIN is a static/global enum used for when a player joins AFTER an npc is summoned. ALL npcs will appear for the player, so use carefully

public enum PacketType {
    NPC_MOVE, NPC_TELEPORT, NPC_ROTATION, NPC_DELETION, NPC_CREATION, NPC_ARM_SWING, NPC_PLAYER_JOIN
}
