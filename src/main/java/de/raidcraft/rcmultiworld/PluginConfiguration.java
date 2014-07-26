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

    private final Map<String, String> worldAliases = new HashMap<>();

    @Setting("shutdown-teleport")
    public boolean shutdownTeleport;
    @Setting("shutdown-teleport-server")
    public String shutdownTeleportServer = "lobby";

    public PluginConfiguration(final RCMultiWorldPlugin plugin) {

        super(plugin, "config.yml");

        this.worldAliases.clear();
    }

    private void loadAliases() {
         /*
         * Config section for world aliases:
         * ---------------------------------
         *  world-aliases:
         *      world: 'RPG Welt'
         *      lobby: 'Lobby'
         */
        final ConfigurationSection aliasSection = getConfigurationSection("aliases");
        if (aliasSection != null) {
            for (final String world : aliasSection.getKeys(false)) {
                this.worldAliases.put(world.toLowerCase(), aliasSection.getString(world));
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

        if (this.worldAliases.isEmpty()) {
            loadAliases();
        }

        worldName = worldName.toLowerCase();
        if (this.worldAliases.containsKey(worldName)) {
            return this.worldAliases.get(worldName);
        }
        return worldName;
    }
}
