package me.shufy.john.biomes;

public interface JohnableBiome {
    void generation(World world);
    void onBiomeTick(int ticks);
}
