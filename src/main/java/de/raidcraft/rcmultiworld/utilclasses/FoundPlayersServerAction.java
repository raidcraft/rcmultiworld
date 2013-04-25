package de.raidcraft.rcmultiworld.utilclasses;

import de.raidcraft.rcmultiworld.events.FoundPlayersServerEvent;

public abstract class FoundPlayersServerAction {

    private long timestamp;

    public FoundPlayersServerAction() {

        timestamp = System.currentTimeMillis();
    }

    public boolean isExpired() {

        if (timestamp < System.currentTimeMillis() - 3 * 1000) {
            return true;
        }
        return false;
    }

    public abstract void process(FoundPlayersServerEvent event);
}