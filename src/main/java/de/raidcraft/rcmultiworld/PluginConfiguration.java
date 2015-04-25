package de.raidcraft.rcmultiworld;

import de.raidcraft.api.config.ConfigurationBase;
import de.raidcraft.api.config.Setting;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Philip
 */
public class PluginConfiguration extends ConfigurationBase<RCMultiWorldPlugin> {

    private Map<String, String> worldAliases = new HashMap<>();

    @Setting("shutdown-teleport")
    public boolean shutdownTeleport = false;
    @Setting("shutdown-teleport-server")
    public String shutdownTeleportServer = "lobby";
    @Setting("broadcast-player-join-quit")
    public boolean broadcastPlayerJoinQuit = true;

    public PluginConfiguration(RCMultiWorldPlugin plugin) {

        super(plugin, "config.yml");

        worldAliases.clear();
    }

    private void loadAliases() {
         /*
         * Config section for world aliases:
         * ---------------------------------
         *  world-aliases:
         *      world: 'RPG Welt'
         *      lobby: 'Lobby'
         */
        ConfigurationSection aliasSection = getConfigurationSection("aliases");
        if(aliasSection != null) {
            for(String world : aliasSection.getKeys(false)) {
                worldAliases.put(world.toLowerCase(), aliasSection.getString(world));
            }
        }
    }

    public int getLeaveTimeout() {
        return getInt("leave-timeout", 10);
    }

    public int getUpdateInterval() {
        return getInt("update-interval", 5);
    }

    public String getAlias(String worldName) {

        if(worldAliases.size() == 0) {
            loadAliases();
        }

        worldName = worldName.toLowerCase();
        if(worldAliases.containsKey(worldName)){
            return  worldAliases.get(worldName);
        }
        return worldName;
    }
}
