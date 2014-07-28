package de.raidcraft.rcmultiworld;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.tables.TTeleportRequest;
import de.raidcraft.reference.Colors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Philip Urban
 * Date: 28.10.13
 * Time: 10:31
 * To change this template use File | Settings | File Templates.
 */
public class TeleportRequestManager {

    private final RCMultiWorldPlugin plugin;

    public TeleportRequestManager(final RCMultiWorldPlugin plugin) {

        this.plugin = plugin;
    }

    public void addRequest(final String player,
                           final String world,
                           final double x,
                           final double y,
                           final double z,
                           final float pitch,
                           final float yaw) {

        // remove old request
        removeRequest(player, world);

        final TTeleportRequest tTeleportRequest = new TTeleportRequest();
        tTeleportRequest.setPlayer(player);
        tTeleportRequest.setWorld(world);
        tTeleportRequest.setX((int) x * 100);
        tTeleportRequest.setY((int) y * 100);
        tTeleportRequest.setZ((int) z * 100);
        tTeleportRequest.setPitch((int) pitch * 100);
        tTeleportRequest.setYaw((int) yaw * 100);
        RaidCraft.getDatabase(RCMultiWorldPlugin.class).save(tTeleportRequest);
    }

    public void removeRequest(final String player, final String world) {

        final TTeleportRequest tTeleportRequest = RaidCraft.getDatabase(RCMultiWorldPlugin.class)
                .find(TTeleportRequest.class).where().ieq("player", player).ieq("world", world).findUnique();

        if (tTeleportRequest != null) {
            RaidCraft.getDatabase(RCMultiWorldPlugin.class).delete(tTeleportRequest);
        }
    }

    public void teleportOnRequest(final Player player) {

        final List<TTeleportRequest> tTeleportRequests = RaidCraft.getDatabase(RCMultiWorldPlugin.class)
                .find(TTeleportRequest.class).where().ieq("player", player.getName()).findList();

        for (final TTeleportRequest tTeleportRequest : tTeleportRequests) {

            if (Bukkit.getWorld(tTeleportRequest.getWorld()) == null) {
                continue;
            }

            player.teleport(tTeleportRequest.getBukkitLocation());
            this.plugin.getTranslationProvider().msg(player, "player.teleported", Colors.Chat.SUCCESS, "You got teleported.");
            RaidCraft.getDatabase(RCMultiWorldPlugin.class).delete(tTeleportRequest);
        }
    }
}
