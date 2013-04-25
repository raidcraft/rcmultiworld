package de.raidcraft.rcmultiworld.players;

import de.raidcraft.rcmultiworld.utilclasses.ServerLocation;
import org.bukkit.GameMode;

/**
 * @author Philip
 */
public class MultiWorldPlayer {

    private String name;
    private boolean online;
    private GameMode gameMode = GameMode.SURVIVAL;
    private ServerLocation beforeTeleportLocation = null;

    public MultiWorldPlayer(String name) {

        this.name = name;
        this.online = true;
    }

    public String getName() {

        return name;
    }

    public boolean isOnline() {

        return online;
    }

    public void setOnline(boolean online) {

        this.online = online;
    }

    public GameMode getGameMode() {

        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {

        this.gameMode = gameMode;
    }

    public ServerLocation getBeforeTeleportLocation() {

        return beforeTeleportLocation;
    }

    public void setBeforeTeleportLocation(ServerLocation beforeTeleportLocation) {

        this.beforeTeleportLocation = beforeTeleportLocation;
    }
}
