package de.raidcraft.rcmultiworld.bungeecord.messages;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Philip
 */
@MessageName("FIND_PLAYERS_SERVER_MESSAGE")
public class FindPlayersServerMessage extends BungeeMessage {

    private String wanted;
    private RCMultiWorldPlugin plugin;

    public FindPlayersServerMessage(String wanted) {

        this.wanted = wanted;
        this.plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    @Override
    protected String encode() {

        return wanted;
    }

    @Override
    public void process() {

        Player player = Bukkit.getPlayer(wanted);
        if(player == null) {
            return;
        }

        plugin.getBungeeManager().sendMessage(player, new SendPlayersServerMessage(player.getName(), Bukkit.getWorlds().get(0).getName(), player.getLocation()));
    }
}
