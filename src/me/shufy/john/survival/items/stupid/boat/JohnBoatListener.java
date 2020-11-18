package me.shufy.john.survival.items.stupid.boat;

import me.shufy.john.Main;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static me.shufy.john.util.JohnUtility.closestPlayerToVehicle;

public class JohnBoatListener implements Listener {

    public static Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onBoatPlace(VehicleCreateEvent e) {
        if (e.getVehicle().getType() == EntityType.BOAT) {
            Boat boat = (Boat) e.getVehicle();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (boat.isDead()) {
                        Player closestPlayerToBoat = closestPlayerToVehicle(boat);
                        if (closestPlayerToBoat != null)
                            closestPlayerToVehicle(boat).getInventory().addItem(JohnBoat.johnBoat());
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 1L);
        }
    }
}
