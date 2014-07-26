package de.raidcraft.rcmultiworld.players;

import de.raidcraft.rcmultiworld.utilclasses.ServerLocation;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.bukkit.GameMode;

/**
 * @author Philip
 */
@Data
public class MultiWorldPlayer {

    @Setter(AccessLevel.NONE)
    private String name;
    private boolean online;
    private GameMode gameMode = GameMode.SURVIVAL;
    private ServerLocation beforeTeleportLocation;

    public MultiWorldPlayer(final String name) {

        this.name = name;
        this.online = true;
    }
}
