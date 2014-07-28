package de.raidcraft.rcmultiworld.restart;

import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.rcmultiworld.commands.RestartCommands;
import de.raidcraft.reference.Colors;
import org.bukkit.Bukkit;

/**
 * @author Philip
 */
public class RestartManager {

    private final RCMultiWorldPlugin plugin;
    private final RestartConfig config;
    private long nextRestart = -1;
    private final ShutdownInfoTask infoTask;

    public RestartManager(final RCMultiWorldPlugin plugin) {

        this.plugin = plugin;
        plugin.registerCommands(RestartCommands.class);
        this.config = plugin.configure(new RestartConfig(plugin));
        this.infoTask = new ShutdownInfoTask(this);
        Bukkit.getScheduler().runTaskTimer(plugin, this.infoTask, 20, 20);
        reload();
    }

    public void reload() {

        this.config.reload();
        this.infoTask.reload();
    }

    public RestartConfig getConfig() {

        return config;
    }

    public long getNextRestart() {

        return nextRestart;
    }

    public void setNextRestart(final long nextRestart) {

        this.nextRestart = nextRestart;
    }

    public void restart() {

        this.plugin.getTranslationProvider().broadcastMessage(
                "server.restart.warning",
                Colors.Chat.WARNING,
                "** SERVER WILL BE RESTARTED **"
        );
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.config.localRestartCommand);
    }
}
