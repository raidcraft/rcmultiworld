package de.raidcraft.rcmultiworld;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.api.WorldManager;
import de.raidcraft.rcmultiworld.tables.TWorld;

import java.util.Optional;
import java.util.UUID;

public class SimpleWorldManager implements WorldManager {

    private RCMultiWorldPlugin plugin;

    public SimpleWorldManager() {
        reload();
    }
    public void reload() {
        plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    public Optional<TWorld> getWorldFromAlias(String alias) {
        return Optional.ofNullable(plugin.getRcDatabase().find(TWorld.class)
                .where()
                .eq("alias", alias).findOne());
    }

    public Optional<TWorld> getWorld(UUID world) {
        return Optional.ofNullable(plugin.getRcDatabase().find(TWorld.class)
                .where()
                .eq("world", world).findOne());
    }

    public Optional<String> getWorldHost(String alias) {
        Optional<TWorld> world = getWorldFromAlias(alias);
        if (!world.isPresent()) {
            return Optional.empty();
        }
        return Optional.ofNullable(world.get().getServer());
    }
}
