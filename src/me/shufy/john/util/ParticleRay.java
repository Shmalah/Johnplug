package me.shufy.john.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class ParticleRay {
    Particle.DustOptions dustOptions;
    Location origin;
    double distance;
    BlockVector ray;

    public ParticleRay(Location origin, Vector ray, double distance, Color particleRayColor, int particleRaySize) {
        this.origin = origin;
        this.distance = distance;
        this.dustOptions = new Particle.DustOptions(particleRayColor, particleRaySize);
        this.ray = ray.toBlockVector();
    }

    public void draw() {
        BlockVector vnR = (this.ray.isNormalized() ? this.ray : this.ray.normalize().toBlockVector());
        for (int i = 0; i < Math.ceil(distance); i++) origin.getWorld().spawnParticle(Particle.REDSTONE, origin.add(vnR).getBlock().getLocation(), 1, dustOptions);
    }
}
