package de.raidcraft.rcmultiworld.players;

import de.raidcraft.rcmultiworld.utilclasses.ServerLocation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;

import java.util.UUID;

/**
 * @author Philip
 */
@Getter
public class MultiWorldPlayer {

    private UUID player;
    @Setter
    private boolean online;
    @Setter
    private GameMode gameMode = GameMode.SURVIVAL;
    @Setter
    private ServerLocation beforeTeleportLocation = null;

    public MultiWorldPlayer(UUID player) {
        this.player = player;
    }
}
