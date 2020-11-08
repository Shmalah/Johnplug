package me.shufy.john.biomes.johnland;

public class JohnLand extends JohnBiome implements JohnableBiome {
    public JohnLand(String name, World world) {
        super(name, world);
    }
    // handles the generation of the biome.. implemented frmo JohnableBiome
    @Override
    void generation(World world) {

    }
    // runs every tick that the biome exists for
    @Override
    void onBiomeTick(int ticks) {

    }
}