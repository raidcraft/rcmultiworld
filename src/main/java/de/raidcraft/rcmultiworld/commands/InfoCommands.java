package de.raidcraft.rcmultiworld.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.rcmultiworld.players.MultiWorldPlayer;
import de.raidcraft.rcmultiworld.players.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Author: Philip
 * Date: 16.10.12 - 19:46
 * Description:
 */
public class InfoCommands {

    public InfoCommands(RCMultiWorldPlugin module) {

    }

    @Command(
            aliases = {"who", "list"},
            desc = "Who's online command"
    )
    public void who(CommandContext context, CommandSender sender) throws CommandException {

        PlayerManager playerManager = RaidCraft.getComponent(RCMultiWorldPlugin.class).getPlayerManager();

        List<MultiWorldPlayer> onlinePlayers = playerManager.getOnlinePlayers();
        String output = ChatColor.GRAY + "Online (" + onlinePlayers.size() + "/" + Bukkit.getMaxPlayers() + "): " + ChatColor.WHITE;
        for(MultiWorldPlayer multiWorldPlayer : onlinePlayers) {
            output += multiWorldPlayer.getName() + ", ";
        }
        output = output.substring(0, output.length() - 2);
        sender.sendMessage(output);
    }
}