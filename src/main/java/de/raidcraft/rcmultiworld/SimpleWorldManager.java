package de.raidcraft.rcmultiworld;

import com.google.common.base.Strings;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.api.WorldManager;
import de.raidcraft.rcmultiworld.tables.TWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class SimpleWorldManager implements WorldManager {

    private RCMultiWorldPlugin plugin;

    public SimpleWorldManager() {
        reload();
    }
    public void reload() {
        plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
        for (World world : Bukkit.getServer().getWorlds()) {
            TWorld alias = plugin.getRcDatabase().find(TWorld.class).where()
                    .eq("alias", world.getName().toLowerCase()).findOneOrEmpty().orElseGet(() -> {
                        TWorld tWorld = new TWorld();
                        tWorld.setAlias(world.getName().toLowerCase());
                        return tWorld;
                    });
            alias.setWorldId(world.getUID());
            alias.setServer(Bukkit.getServerName());
            alias.setFolder(Bukkit.getServer().getWorldContainer().getAbsolutePath());
            plugin.getRcDatabase().save(alias);
        }
    }

    public Optional<TWorld> getWorldFromAlias(String alias) {
        if (Strings.isNullOrEmpty(alias)) return Optional.empty();
        alias = alias.toLowerCase();
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
