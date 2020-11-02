package me.shufy.john.randomevents.appear;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface JohnAppearable {
    void beforeAppearance(Player appearTo);
    void duringAppearance(Location appearLocation);
    void afterAppearance(Location playerLocation);
}
