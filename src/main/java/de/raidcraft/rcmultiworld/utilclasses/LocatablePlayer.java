package de.raidcraft.rcmultiworld.utilclasses;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Philip
 */
public class LocatablePlayer implements Locatable {

    private Player player;

    public LocatablePlayer(Player player) {

        this.player = player;
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }

    public Player getPlayer() {

        return player;
    }
}
