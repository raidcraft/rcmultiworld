package de.raidcraft.rcmultiworld.utilclasses;

import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.bungeecord.messages.TeleportToPlayerMessage;
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

        bungeeManager.sendMessage(player, new TeleportToPlayerMessage(player.getName(), event.getPlayer()));
        BungeeCordUtil.changeServer(player, event.getServer());
    }
}
