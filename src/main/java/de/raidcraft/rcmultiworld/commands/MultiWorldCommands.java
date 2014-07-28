package de.raidcraft.rcmultiworld.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.reference.Colors;
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
            aliases = {"rcmultiworld", "multiworld", "mv", "mw"},
            desc = "Main command"
    )
    @NestedCommand(NestedLootCommands.class)
    public void rcmultiworld(final CommandContext context, final CommandSender sender) throws CommandException {

    }

    public static class NestedLootCommands {

        private final RCMultiWorldPlugin plugin;

        public NestedLootCommands(final RCMultiWorldPlugin plugin) {

            this.plugin = plugin;
        }

        @Command(
                aliases = {"reload"},
                desc = "Reloads config and shit"
        )
        @CommandPermissions("rcmultiworld.reload")
        public void reload(final CommandContext context, final CommandSender sender) throws CommandException {

            RaidCraft.getComponent(RCMultiWorldPlugin.class).reload();
            this.plugin.getTranslationProvider().msg(
                    sender,
                    "command.reload.success",
                    Colors.Chat.SUCCESS,
                    "[RCMultiWorld] Reload complete.");
        }
    }
}
