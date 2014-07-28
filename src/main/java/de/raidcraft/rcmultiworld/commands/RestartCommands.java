package de.raidcraft.rcmultiworld.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.reference.Colors;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author Philip
 */
public class RestartCommands {

    private final RCMultiWorldPlugin plugin;

    public RestartCommands(final RCMultiWorldPlugin plugin) {

        this.plugin = plugin;
    }

    @Command(
            aliases = {"restart", "neustart"},
            desc = "Server restart",
            flags = "ai"
    )
    @CommandPermissions("rcmultiworld.restart")
    public void restart(final CommandContext context, final CommandSender sender) throws CommandException {

        final long restartDelay = this.plugin.getRestartManager().getConfig().restartDelay * 1000;
        this.plugin.getRestartManager().setNextRestart(System.currentTimeMillis() + restartDelay);

        this.plugin.getTranslationProvider().msg(
                sender,
                "command.restart.success",
                Colors.Chat.SUCCESS,
                "The Server will be restarted."
        );
    }

    @Command(
            aliases = {"timedrestart", "reschedulerestart"},
            desc = "Scheduled restart",
            flags = "a"
    )
    @CommandPermissions("rcmultiworld.restart")
    public void scheduled(CommandContext context, CommandSender sender) throws CommandException {

        int hour = 0;
        int minute = 0;
        int second = 0;

        //TODO implement

        sender.sendMessage(ChatColor.YELLOW + "Serverneustart um " + hour + ":" + minute + ":" + second + "Uhr");
    }
}
