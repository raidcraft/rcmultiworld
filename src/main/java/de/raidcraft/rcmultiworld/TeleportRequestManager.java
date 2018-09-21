package de.raidcraft.rcmultiworld;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.tables.TTeleportRequest;
import de.raidcraft.rcmultiworld.tables.TWorld;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

/**
 * @author Dragonfire
 */
public class TeleportRequestManager {

    private RCMultiWorldPlugin plugin;

    public TeleportRequestManager() {
        reload();
    }

    public void reload() {
        plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    public boolean addRequest(Player player, String worldAlias, double x, double y, double z, float pitch, float yaw) {

        Optional<TWorld> world = plugin.getSimpleWorldManager().getWorldFromAlias(worldAlias);
        if (!world.isPresent()) {
            plugin.warning("addRequest: worldAlias " + worldAlias + " not found for Teleport Request from"
                    + player.getDisplayName());

            return false;
        }

        // remove old request
        removeRequest(player, worldAlias);

        TTeleportRequest tTeleportRequest = new TTeleportRequest();
        tTeleportRequest.setPlayer(player.getUniqueId());
        tTeleportRequest.setWorld(world.get());
        tTeleportRequest.setX((int) x);
        tTeleportRequest.setY((int) y);
        tTeleportRequest.setZ((int) z);
        tTeleportRequest.setYaw((int) yaw);
        tTeleportRequest.setPitch((int) pitch);

        plugin.getDatabase().save(tTeleportRequest);
        return true;
    }

    /**
     * @return if a request was removed
     */
    public boolean removeRequest(Player player, String worldAlias) {
        Optional<TWorld> world = plugin.getSimpleWorldManager().getWorldFromAlias(worldAlias);
        if (!world.isPresent()) {
            plugin.warning("removeRequest: worldAlias " + worldAlias + " not found for Teleport Request from"
                    + player.getDisplayName());
            return false;
        }

        List<TTeleportRequest> requests = plugin.getDatabase().find(TTeleportRequest.class)
                .where()
                .eq("player", player.getUniqueId()).findList();
        if (requests != null) {
            plugin.getDatabase().delete(requests);
            return true;
        }
        return false;
    }

    public boolean teleportOnRequest(Player player) {

        TTeleportRequest tTeleportRequest = plugin.getDatabase().find(TTeleportRequest.class)
                .where().eq("player", player.getUniqueId()).findOne();

        if (tTeleportRequest == null) {
            plugin.info("teleportOnRequest: no request found for " + player.getDisplayName());
            return false;
        }
        Optional<Location> location = tTeleportRequest.getLocation();
        if (!location.isPresent()) {
            plugin.warning("teleportOnRequest: location not found for " + player.getDisplayName());
            return false;
        }

        player.teleport(location.get());
        player.sendMessage(ChatColor.YELLOW + "Teleported.");
        plugin.getDatabase().delete(tTeleportRequest);
        return true;
    }
}
