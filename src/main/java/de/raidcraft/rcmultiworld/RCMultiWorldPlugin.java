package de.raidcraft.rcmultiworld;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.BasePlugin;
import de.raidcraft.rcmultiworld.bungeecord.BungeeListener;
import de.raidcraft.rcmultiworld.commands.InfoCommands;
import de.raidcraft.rcmultiworld.commands.MultiWorldCommands;
import de.raidcraft.rcmultiworld.commands.TeleportCommand;
import de.raidcraft.rcmultiworld.listener.FoundPlayersServerListener;
import de.raidcraft.rcmultiworld.listener.PlayerListener;
import de.raidcraft.rcmultiworld.players.PlayerManager;
import de.raidcraft.rcmultiworld.restart.RestartManager;
import de.raidcraft.rcmultiworld.tables.WorldInfoTable;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * @author Philip
 */
public class RCMultiWorldPlugin extends BasePlugin {

    private BungeeManager bungeeManager;
    private PlayerManager playerManager;
    private RestartManager restartManager;
    private PluginConfiguration config;

    @Override
    public void enable() {

        registerEvents(new PlayerListener());
        registerEvents(new FoundPlayersServerListener());
        registerCommands(MultiWorldCommands.class);
        registerCommands(TeleportCommand.class);
        registerCommands(InfoCommands.class);

        registerTable(WorldInfoTable.class, new WorldInfoTable());

        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener());

        config = configure(new PluginConfiguration(this), false);
        this.bungeeManager = new BungeeManager(this, getDescription().getName());
        this.playerManager = new PlayerManager(this);
        this.restartManager = new RestartManager(this);

        reload();
    }

    @Override
    public void reload() {

        restartManager.reload();
        config.reload();

        String mainWorld = Bukkit.getWorlds().get(0).getName();
        for(World world : Bukkit.getWorlds()) {
            RaidCraft.getTable(WorldInfoTable.class).addWorld(mainWorld, world.getName());
        }
    }

    @Override
    public void disable() {
    }

    public PluginConfiguration getConfig() {

        return config;
    }

    public BungeeManager getBungeeManager() {

        return bungeeManager;
    }

    public PlayerManager getPlayerManager() {

        return playerManager;
    }

    public RestartManager getRestartManager() {

        return restartManager;
    }
}
