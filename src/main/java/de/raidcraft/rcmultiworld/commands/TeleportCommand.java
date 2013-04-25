package de.raidcraft.rcmultiworld.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.rcmultiworld.bungeecord.messages.FindPlayersServerMessage;
import de.raidcraft.rcmultiworld.bungeecord.messages.SaveReturnLocationMessage;
import de.raidcraft.rcmultiworld.bungeecord.messages.TeleportToCoordsMessage;
import de.raidcraft.rcmultiworld.bungeecord.messages.TeleportToPlayerMessage;
import de.raidcraft.rcmultiworld.events.FoundPlayersServerEvent;
import de.raidcraft.rcmultiworld.listener.FoundPlayersServerListener;
import de.raidcraft.rcmultiworld.players.MultiWorldPlayer;
import de.raidcraft.rcmultiworld.utilclasses.FoundPlayersServerAction;
import de.raidcraft.rcmultiworld.utilclasses.ServerLocation;
import de.raidcraft.util.BungeeCordUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Author: Philip
 * Date: 16.10.12 - 19:46
 * Description:
 */
public class TeleportCommand {

    public TeleportCommand(RCMultiWorldPlugin module) {

    }

    @Command(
            aliases = {"tp", "teleport"},
            desc = "Teleport command",
            min = 1
    )
         @CommandPermissions("rcmultiworld.tp")
         public void teleport(CommandContext context, CommandSender sender) throws CommandException {

        Player player = (Player)sender;
        RCMultiWorldPlugin plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);

        if(context.argsLength() == 1) {
            // teleport to coordinates
            if(context.getString(0).contains(",")) {
                String[] coords = context.getString(0).split(",");
                try {
                    double x = Double.parseDouble(coords[0]);
                    double y = Double.parseDouble(coords[1]);
                    double z = Double.parseDouble(coords[2]);
                    String world = player.getWorld().getName();
                    if(coords.length > 3) {
                        world = coords[3];
                    }

                    if(world.equalsIgnoreCase(player.getWorld().getName())) {
                        Location location = new Location(player.getWorld(), x, y, z);
                        plugin.getPlayerManager().getPlayer(player.getName()).setBeforeTeleportLocation(new ServerLocation(player.getLocation()));
                        plugin.getBungeeManager().sendMessage(player, new SaveReturnLocationMessage(player.getName(), player.getLocation()));
                        player.teleport(location);
                        player.sendMessage(ChatColor.YELLOW + "Teleported.");
                    }
                    else {
                        BungeeCordUtil.changeServer(player, world);
                        plugin.getBungeeManager().sendMessage(player, new TeleportToCoordsMessage(player.getName(),
                                world, coords[0], coords[1], coords[2], "0", "0"));
                    }
                    return;
                }
                catch(NumberFormatException | IndexOutOfBoundsException e) {
                    throw new CommandException("Die Koordinaten haben das falsche Format! ('x,y,z' erwartet)");
                }
            }

            MultiWorldPlayer multiWorldPlayer = plugin.getPlayerManager().getPlayer(context.getString(0));
            if(multiWorldPlayer == null || !multiWorldPlayer.isOnline()) {
                throw new CommandException("Kein passender Spieler gefunden.");
            }

            Player targetPlayer = Bukkit.getPlayer(multiWorldPlayer.getName());
            if(targetPlayer != null) {
                plugin.getPlayerManager().getPlayer(player.getName()).setBeforeTeleportLocation(new ServerLocation(player.getLocation()));
                plugin.getBungeeManager().sendMessage(player, new SaveReturnLocationMessage(player.getName(), player.getLocation()));
                player.teleport(targetPlayer);
                player.sendMessage(ChatColor.YELLOW + "Teleported.");
                return;
            }

            plugin.getPlayerManager().getPlayer(player.getName()).setBeforeTeleportLocation(new ServerLocation(player.getLocation()));
            plugin.getBungeeManager().sendMessage(player, new SaveReturnLocationMessage(player.getName(), player.getLocation()));
            FoundPlayersServerListener.registerAction(multiWorldPlayer.getName(), new TeleportOnServerFoundAction(player, plugin.getBungeeManager()));
            plugin.getBungeeManager().sendMessage(player, new FindPlayersServerMessage(multiWorldPlayer.getName()));
        }
    }

    @Command(
            aliases = {"summon", "s", "tphere"},
            desc = "Summon command",
            min = 1
    )
    @CommandPermissions("rcmultiworld.tp")
    public void summon(CommandContext context, CommandSender sender) throws CommandException {

        Player player = (Player)sender;
        RCMultiWorldPlugin plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);

        if(context.argsLength() == 1) {

            MultiWorldPlayer multiWorldPlayer = plugin.getPlayerManager().getPlayer(context.getString(0));
            if(multiWorldPlayer == null || !multiWorldPlayer.isOnline()) {
                throw new CommandException("Kein passender Spieler gefunden.");
            }

            Player targetPlayer = Bukkit.getPlayer(context.getString(0));
            if(targetPlayer != null) {
                plugin.getPlayerManager().getPlayer(targetPlayer.getName()).setBeforeTeleportLocation(new ServerLocation(targetPlayer.getLocation()));
                plugin.getBungeeManager().sendMessage(player, new SaveReturnLocationMessage(targetPlayer.getName(), targetPlayer.getLocation()));
                targetPlayer.teleport(player);
                targetPlayer.sendMessage(ChatColor.YELLOW + "Teleported.");
                return;
            }

            plugin.getBungeeManager().sendMessage(player, new TeleportToCoordsMessage(context.getString(0),
                    new ServerLocation(player.getLocation())));
        }
    }

    @Command(
            aliases = {"return"},
            desc = "Return command"
    )
    @CommandPermissions("rcmultiworld.return")
    public void teleportReturn(CommandContext context, CommandSender sender) throws CommandException {

        RCMultiWorldPlugin plugin = RaidCraft.getComponent(RCMultiWorldPlugin.class);
        MultiWorldPlayer multiWorldPlayer = plugin.getPlayerManager().getPlayer(sender.getName());
        if(multiWorldPlayer == null) return;

        ServerLocation serverLocation = multiWorldPlayer.getBeforeTeleportLocation();
        if(serverLocation == null) {
            throw new CommandException("Keine Return Location gespeichert!");
        }

        Player player = (Player)sender;
        Location location = serverLocation.getBukkitLocation();
        if(location != null) {
            player.teleport(location);
            player.sendMessage(ChatColor.YELLOW + "Teleported.");
        }
        else {
            plugin.getBungeeManager().sendMessage(player, new TeleportToCoordsMessage(player.getName(), serverLocation));
        }
    }

    public class TeleportOnServerFoundAction extends FoundPlayersServerAction {

        private Player player;
        private BungeeManager bungeeManager;

        public TeleportOnServerFoundAction(Player player, BungeeManager bungeeManager) {

            this.player = player;
            this.bungeeManager = bungeeManager;
        }

        @Override
        public void process(FoundPlayersServerEvent event) {

            bungeeManager.sendMessage(player, new TeleportToPlayerMessage(player.getName(), event.getPlayer()));
            BungeeCordUtil.changeServer(player, event.getServer());
        }
    }
}