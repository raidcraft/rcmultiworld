package de.raidcraft.rcmultiworld.restart;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author Philip
 */
public class RestartCommands {

    public RestartCommands(RCMultiWorldPlugin module) {

    }

    @Command(
            aliases = {"restart", "neustart"},
            desc = "Server restart",
            flags = "ai"
    )
     @CommandPermissions("rcmultiworld.restart")
     public void restart(CommandContext context, CommandSender sender) throws CommandException {

        RCMultiWorldPlugin plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
        plugin.getRestartManager().setNextRestart(System.currentTimeMillis() + plugin.getRestartManager().getConfig().restartDelay * 1000);
        sender.sendMessage(ChatColor.YELLOW + "Neustart wird ausgeführt!");
    }

    @Command(
            aliases = {"timedrestart", "reschedulerestart"},
            desc = "Scheduled restart",
            flags = "a"
    )
    @CommandPermissions("rcmultiworld.restart")
    public void scheduled(CommandContext context, CommandSender sender) throws CommandException {


    }
}
