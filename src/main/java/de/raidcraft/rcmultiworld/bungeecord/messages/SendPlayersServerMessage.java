package de.raidcraft.rcmultiworld.bungeecord.messages;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.rcmultiworld.events.FoundPlayersServerEvent;
import org.bukkit.Bukkit;

/**
 * @author Philip
 */
@MessageName("SEND_PLAYERS_SERVER_MESSAGE")
public class SendPlayersServerMessage extends BungeeMessage {

    private String wanted;
    private String server;
    private RCMultiWorldPlugin plugin;

    public SendPlayersServerMessage(String wanted, String server) {

        this.wanted = wanted;
        this.server = server;
        this.plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    @Override
    protected String encode() {

        return wanted + BungeeManager.DELIMITER + server;
    }

    @Override
    public void process() {

        Bukkit.getPluginManager().callEvent(new FoundPlayersServerEvent(wanted, server));
    }
}
