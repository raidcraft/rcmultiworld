package de.raidcraft.rcmultiworld.bungeecord.messages;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.rcmultiworld.listener.PlayerListener;
import de.raidcraft.rcmultiworld.utilclasses.LocatablePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Philip
 */
@MessageName("TELEPORT_TO_PLAYER_MESSAGE")
public class TeleportToPlayerMessage extends BungeeMessage {

    private String playerName;
    private String targetPlayerName;
    private RCMultiWorldPlugin plugin;

    public TeleportToPlayerMessage(String player, String targetPlayer) {

        this.playerName = player;
        this.targetPlayerName = targetPlayer;
        this.plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    @Override
    protected String encode() {

        return playerName + BungeeManager.DELIMITER + targetPlayerName;
    }

    @Override
    public void process() {

        Player teleportPlayer = Bukkit.getPlayer(playerName);
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if(targetPlayer == null) {
            return;
        }

        if(teleportPlayer != null) {
            teleportPlayer.teleport(targetPlayer.getLocation());
            teleportPlayer.sendMessage(ChatColor.YELLOW + "Teleported.");
        }
        else {
            PlayerListener.enqueuePlayer(playerName, new LocatablePlayer(targetPlayer));
        }

    }
}
