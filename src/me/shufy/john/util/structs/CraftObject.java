package me.shufy.john.util.structs;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class CraftObject {

    public Entity entity = null;
    public LivingEntity livingEntity = null;
    public Block block = null;

    public CraftObject(Entity entity) {
        this.entity = entity;
    }
    public CraftObject(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }
    public CraftObject(Block block) {
        this.block = block;
    }

    public boolean isLivingEntity() {
        return livingEntity != null;
    }

    public boolean isBlock() {
        return block != null;
    }

    public boolean isEntity() {
        return entity != null || livingEntity != null;
    }
}
