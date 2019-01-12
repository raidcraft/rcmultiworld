package de.raidcraft.rcmultiworld;

import de.raidcraft.api.BasePlugin;
import de.raidcraft.api.action.ActionAPI;
import de.raidcraft.rcmultiworld.actions.ChangeServerAction;
import de.raidcraft.rcmultiworld.api.WorldManager;
import de.raidcraft.rcmultiworld.bungeecord.BungeeListener;
import de.raidcraft.rcmultiworld.commands.MultiWorldCommands;
import de.raidcraft.rcmultiworld.commands.TeleportCommand;
import de.raidcraft.rcmultiworld.listener.FoundPlayersServerListener;
import de.raidcraft.rcmultiworld.listener.PlayerListener;
import de.raidcraft.rcmultiworld.players.PlayerManager;
import de.raidcraft.rcmultiworld.restart.RestartManager;
import de.raidcraft.rcmultiworld.tables.TTeleportRequest;
import de.raidcraft.rcmultiworld.tables.TWorld;
import de.raidcraft.util.BungeeCordUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
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
    private SimpleWorldManager simpleWorldManager;

    @Override
    public void enable() {

        registerEvents(new PlayerListener());
        registerEvents(new FoundPlayersServerListener());
        registerCommands(MultiWorldCommands.class);
        registerCommands(TeleportCommand.class);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener());

        config = configure(new PluginConfiguration(this));
        this.bungeeManager = new BungeeManager(this, getDescription().getName());
        this.playerManager = new PlayerManager();
        this.restartManager = new RestartManager(this);
        this.teleportRequestManager = new TeleportRequestManager();
        this.simpleWorldManager = new SimpleWorldManager();

        ActionAPI.register(this)
                .global()
                .action(new ChangeServerAction());

        reload();
    }

    @Override
    public void reload() {

        restartManager.reload();
        config.reload();
        this.teleportRequestManager.reload();
        this.simpleWorldManager.reload();
    }

    @Override
    public void disable() {

        if (getConfig().shutdownTeleport) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                BungeeCordUtil.changeServer(player, getConfig().shutdownTeleportServer);
                // chat messages not possible here!!!
            }
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        return Arrays.asList(TWorld.class, TTeleportRequest.class);
    }

    public WorldManager getWorldManager() {
        return this.simpleWorldManager;
    }
}
