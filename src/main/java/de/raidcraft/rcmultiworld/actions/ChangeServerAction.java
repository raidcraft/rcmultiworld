package de.raidcraft.rcmultiworld.actions;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.locations.ConfiguredLocation;
import de.raidcraft.api.locations.Locations;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.rcmultiworld.SimpleWorldManager;
import de.raidcraft.rcmultiworld.tables.TWorld;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ChangeServerAction implements Action<Player> {

    @Information(
            value = "server",
            desc = "Changes the server of the player.",
            aliases = {"changeserver"},
            conf = {
                    "world: world",
                    "x: coordinate",
                    "y: coordinate",
                    "z: coordinate",
                    "pitch: pitch",
                    "yaw: yaw"
            }
    )
    @Override
    public void accept(Player player, ConfigurationSection config) {

        RCMultiWorldPlugin plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
        Optional<TWorld> world = plugin.getSimpleWorldManager()
                .getWorldFromAlias(config.getString("world"));
        if (!world.isPresent()) {
            RaidCraft.LOGGER.warning("Unknown or invalid world in cross server teleport request! World is " + config.getString("world"));
            return;
        }
        plugin.getTeleportRequestManager().addRequest(player,
                world.get().getAlias(),
                config.getDouble("x", player.getLocation().getX()),
                config.getDouble("y", player.getLocation().getY()),
                config.getDouble("z", player.getLocation().getZ()),
                (float) config.getDouble("pitch", player.getLocation().getPitch()),
                (float) config.getDouble("yaw", player.getLocation().getYaw())
        );
    }
}
