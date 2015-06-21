package de.raidcraft.rcmultiworld.commands;

import com.sk89q.minecraft.util.commands.*;
import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Author: Philip
 * Date: 16.10.12 - 19:46
 * Description:
 */
public class MultiWorldCommands {

    public MultiWorldCommands(RCMultiWorldPlugin module) {

    }

    @Command(
            aliases = {"who", "online", "list"},
            desc = "Who alias command"
    )
    public void who(CommandContext context, CommandSender sender) throws CommandException {

        Bukkit.dispatchCommand(sender, "glist");
    }

    @Command(
            aliases = {"rcmultiworld", "multiworld", "mv", "mw"},
            desc = "Main command"
    )
    @NestedCommand(NestedMultiworldCommands.class)
    public void rcmultiworld(CommandContext context, CommandSender sender) throws CommandException {
    }

    public static class NestedMultiworldCommands {

        private final RCMultiWorldPlugin module;

        public NestedMultiworldCommands(RCMultiWorldPlugin module) {

            this.module = module;
        }

        @Command(
                aliases = {"reload"},
                desc = "Reloads config and shit"
        )
        @CommandPermissions("rcmultiworld.reload")
        public void reload(CommandContext context, CommandSender sender) throws CommandException {

            RaidCraft.getComponent(RCMultiWorldPlugin.class).reload();
            sender.sendMessage(ChatColor.GREEN + "RCMultiWorld wurde neugeladen!");
        }
    }
}
