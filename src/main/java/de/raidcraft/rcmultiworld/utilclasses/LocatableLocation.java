package de.raidcraft.rcmultiworld.utilclasses;

import org.bukkit.Location;

/**
 * TODO: Check if it is still in use.
 *
 * @author Philip
 */
public class LocatableLocation implements Locatable {

    private final Location location;

    public LocatableLocation(final Location location) {

        this.location = location;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }
}
