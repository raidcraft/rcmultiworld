package de.raidcraft.rcmultiworld.restart;

import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @author Philip
 */
public class RestartManager {

    private RCMultiWorldPlugin plugin;
    private RestartConfig config;
    private long nextRestart = -1;
    private ShutdownInfoTask infoTask;

    public RestartManager(RCMultiWorldPlugin plugin) {

        this.plugin = plugin;
        plugin.registerCommands(RestartCommands.class);
        config = plugin.configure(new RestartConfig(plugin));
        infoTask = new ShutdownInfoTask(this);
        Bukkit.getScheduler().runTaskTimer(plugin, infoTask, 20, 20);
        reload();
    }

    public void reload() {

        config.reload();
        infoTask.reload();
    }

    public RestartConfig getConfig() {

        return config;
    }

    public long getNextRestart() {

        return nextRestart;
    }

    public void setNextRestart(long nextRestart) {

        this.nextRestart = nextRestart;
    }

    public void restart() {

        Bukkit.broadcastMessage(ChatColor.GOLD + "** " + ChatColor.DARK_RED + "Server wird neugestartet " + ChatColor.GOLD + "**");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), config.localRestartCommand);
    }
}
