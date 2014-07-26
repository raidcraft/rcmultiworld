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
import de.raidcraft.rcmultiworld.tables.TTeleportRequest;
import de.raidcraft.rcmultiworld.tables.WorldInfoTable;
import de.raidcraft.util.BungeeCordUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip
 */
@Getter
public class RCMultiWorldPlugin extends BasePlugin {

    private BungeeManager bungeeManager;
    private PlayerManager playerManager;
    private RestartManager restartManager;
    private TeleportRequestManager teleportRequestManager;
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

        this.config = this.configure(new PluginConfiguration(this));
        this.bungeeManager = new BungeeManager(this, this.getDescription().getName());
        this.playerManager = new PlayerManager(this);
        this.restartManager = new RestartManager(this);
        this.teleportRequestManager = new TeleportRequestManager(this);

        reload();
    }

    @Override
    public void reload() {

        this.restartManager.reload();
        this.config.reload();

        final String mainWorld = Bukkit.getWorlds().get(0).getName();
        for (final World world : Bukkit.getWorlds()) {
            RaidCraft.getTable(WorldInfoTable.class).addWorld(mainWorld, world.getName());
        }
    }

    @Override
    public void disable() {

        if (getConfig().shutdownTeleport) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                BungeeCordUtil.changeServer(player, getConfig().shutdownTeleportServer);
                // chat messages not possible here!!!
            }
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {

        final List<Class<?>> databases = new ArrayList<>();
        databases.add(TTeleportRequest.class);

        return databases;
    }
}
