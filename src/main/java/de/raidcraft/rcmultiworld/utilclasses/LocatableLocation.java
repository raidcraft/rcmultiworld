package de.raidcraft.rcmultiworld.utilclasses;

import org.bukkit.Location;

/**
 * @author Philip
 */
public class LocatableLocation implements Locatable {

    private Location location;

    public LocatableLocation(Location location) {

        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }
}
