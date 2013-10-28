package de.raidcraft.rcmultiworld;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.tables.TTeleportRequest;
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

    private RCMultiWorldPlugin plugin;

    public TeleportRequestManager(RCMultiWorldPlugin plugin) {

        this.plugin = plugin;
    }

    public void addRequest(String player, String world, double x, double y, double z, float pitch, float yaw) {

        // remove old request
        removeRequest(player, world);

        TTeleportRequest tTeleportRequest = new TTeleportRequest();
        tTeleportRequest.setPlayer(player);
        tTeleportRequest.setWorld(world);
        tTeleportRequest.setX((int)x * 100);
        tTeleportRequest.setY((int) y * 100);
        tTeleportRequest.setZ((int) z * 100);
        tTeleportRequest.setPitch((int)pitch * 100);
        tTeleportRequest.setYaw((int)yaw * 100);
        RaidCraft.getDatabase(RCMultiWorldPlugin.class).save(tTeleportRequest);
    }

    public void removeRequest(String player, String world) {

        TTeleportRequest tTeleportRequest = RaidCraft.getDatabase(RCMultiWorldPlugin.class)
                .find(TTeleportRequest.class).where().ieq("player", player).ieq("world", world).findUnique();
        if(tTeleportRequest != null) {
            RaidCraft.getDatabase(RCMultiWorldPlugin.class).delete(tTeleportRequest);
        }
    }

    public void teleportOnRequest(Player player) {

        List<TTeleportRequest> tTeleportRequests = RaidCraft.getDatabase(RCMultiWorldPlugin.class)
                .find(TTeleportRequest.class).where().ieq("player", player.getName()).findList();

        for(TTeleportRequest tTeleportRequest : tTeleportRequests) {

            if(Bukkit.getWorld(tTeleportRequest.getWorld()) == null) continue;

            //TODO
        }
    }
}
