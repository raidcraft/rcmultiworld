package de.raidcraft.rcmultiworld.bungeecord.messages;

import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.util.BungeeCordUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public class ChangeServerMessage extends BungeeMessage {

    private String targetPlayer;
    private String targetServer;

    public ChangeServerMessage(String targetPlayer, String targetServer) {

        this.targetPlayer = targetPlayer;
        this.targetServer = targetServer;
    }

    @Override
    protected String encode() {

        return targetPlayer + BungeeManager.DELIMITER + targetServer;
    }

    @Override
    public void process() {

        Player player = Bukkit.getPlayer(targetPlayer);
        if(player == null) {
            return;
        }

        BungeeCordUtil.changeServer(player, targetServer);
    }
}
