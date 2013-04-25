package de.raidcraft.rcmultiworld.bungeecord.messages;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.util.StringEncodingUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * @author Philip
 */
@MessageName("EXECUTE_COMMAND")
public class ExecuteCommandMessage extends BungeeMessage {

    private String command;
    private String server;
    private RCMultiWorldPlugin plugin;

    public ExecuteCommandMessage(String server, String command) {

        this.command = command;
        this.server = server;
        plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
    }

    @Override
    protected String encode() {

        return server + BungeeManager.DELIMITER + StringEncodingUtil.encode(command);
    }

    @Override
    public void process() {

        for(World world : Bukkit.getWorlds()) {

            if(world.getName().equalsIgnoreCase(server)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringEncodingUtil.decode(command));
            }
        }
    }
}
