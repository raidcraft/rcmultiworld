package de.raidcraft.rcmultiworld.utilclasses;

import de.raidcraft.rcmultiworld.events.FoundPlayersServerEvent;

public abstract class FoundPlayersServerAction {

    private final long timestamp;

    public FoundPlayersServerAction() {

        this.timestamp = System.currentTimeMillis();
    }

    public boolean isExpired() {

        if (this.timestamp < (System.currentTimeMillis() - (3 * 1000))) {
            return true;
        }
        return false;
    }

    public abstract void process(FoundPlayersServerEvent event);
}