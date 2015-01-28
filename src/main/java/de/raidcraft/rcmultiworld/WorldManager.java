package de.raidcraft.rcmultiworld;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.tables.TWorld;

import java.util.Optional;
import java.util.UUID;

public class WorldManager {

    private RCMultiWorldPlugin plugin;

    public WorldManager() {
        reload();
    }
    public void reload() {
        plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    public Optional<TWorld> getWorldFromAlias(String alias) {
        return Optional.ofNullable(plugin.getDatabase().find(TWorld.class)
                .where()
                .eq("alias", alias).findUnique());
    }

    public Optional<TWorld> getWorld(UUID world) {
        return Optional.ofNullable(plugin.getDatabase().find(TWorld.class)
                .where()
                .eq("world", world).findUnique());
    }
}
