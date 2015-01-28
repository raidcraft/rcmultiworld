package de.raidcraft.rcmultiworld.utilclasses;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.rcmultiworld.events.FoundPlayersServerEvent;
import de.raidcraft.util.BungeeCordUtil;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public class TeleportOnServerFoundAction extends FoundPlayersServerAction {

    private Player player;
    private BungeeManager bungeeManager;

    public TeleportOnServerFoundAction(Player player, BungeeManager bungeeManager) {

        this.player = player;
        this.bungeeManager = bungeeManager;
    }

    @Override
    public void process(FoundPlayersServerEvent event) {

        RaidCraft.getComponent(RCMultiWorldPlugin.class).getTeleportRequestManager()
                .addRequest(player, event.getWorld(), event.getX(), event.getY(), event.getZ(), event.getPitch(), event.getYaw());
        BungeeCordUtil.changeServer(player, event.getServer());
    }
}
