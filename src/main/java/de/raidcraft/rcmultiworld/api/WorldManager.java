package de.raidcraft.rcmultiworld.api;

import java.util.Optional;

public interface WorldManager {

    public void reload();

    public Optional<String> getWorldHost(String alias);
}
