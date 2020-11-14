package me.shufy.john.biomes;

// TODO world needs imported, doing this from my macbook on vscode so theres no auto imports..
public class JohnBiome {
    public static final Main plugin = Main.getPlugin(Main.class);
    String name;
    World world;
    BukkitTask biomeListener;
    public JohnBiome(String name, World world) {
        this.name = name;
        this.world = world;
        if (generation(this.world)) {
            this.biomeListener = new BukkitRunnable() {
                private int ticks = 0;
                @Override
                public void run() {
                    onBiomeTick(ticks);
                    ticks++;
                }
            }.runTaskTimer(plugin, 0, 1L);
        }
    }
    public boolean generation() {
        
    }
    public void onBiomeTick() {

    }
}
