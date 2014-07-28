package de.raidcraft.rcmultiworld.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.rcmultiworld.bungeecord.messages.ChangeServerMessage;
import de.raidcraft.rcmultiworld.bungeecord.messages.FindPlayersServerMessage;
import de.raidcraft.rcmultiworld.bungeecord.messages.SaveReturnLocationMessage;
import de.raidcraft.rcmultiworld.listener.FoundPlayersServerListener;
import de.raidcraft.rcmultiworld.players.MultiWorldPlayer;
import de.raidcraft.rcmultiworld.tables.WorldInfoTable;
import de.raidcraft.rcmultiworld.utilclasses.ServerLocation;
import de.raidcraft.rcmultiworld.utilclasses.TeleportOnServerFoundAction;
import de.raidcraft.reference.Colors;
import de.raidcraft.util.BungeeCordUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Author: Philip
 * Date: 16.10.12 - 19:46
 * Description:
 */
public class TeleportCommand {

    private final RCMultiWorldPlugin plugin;

    public TeleportCommand(final RCMultiWorldPlugin plugin) {

        this.plugin = plugin;
    }

    @Command(
            aliases = {"tp", "teleport"},
            desc = "Teleport command",
            min = 1
    )
         @CommandPermissions("rcmultiworld.tp")
         public void teleport(final CommandContext context, final CommandSender sender) throws CommandException {

        if(sender instanceof Player) {
            final Player player = (Player)sender;

            if(context.argsLength() == 1) {
                // teleport to coordinates
                if(context.getString(0).contains(",")) {
                    final String[] coords = context.getString(0).split(",");
                    try {
                        final double x = Double.parseDouble(coords[0]);
                        final double y = Double.parseDouble(coords[1]);
                        final double z = Double.parseDouble(coords[2]);
                        String world = player.getWorld().getName();
                        if(coords.length > 3) {
                            world = coords[3];
                        }

                        if(world.equalsIgnoreCase(player.getWorld().getName())) {
                            Location location = new Location(player.getWorld(), x, y, z);
                            this.plugin.getPlayerManager().getPlayer(player.getName()).setBeforeTeleportLocation(new ServerLocation(player.getLocation()));
                            this.plugin.getBungeeManager().sendMessage(player, new SaveReturnLocationMessage(player.getName(), player.getLocation()));
                            player.teleport(location);
                            this.plugin.getTranslationProvider().msg(
                                    sender,
                                    "command.teleport.sender.success",
                                    Colors.Chat.SUCCESS,
                                    "You teleported %1$s.",
                                    player.getName());
                            this.plugin.getTranslationProvider().msg(
                                    sender,
                                    "command.teleport.player.success",
                                    Colors.Chat.SUCCESS,
                                    "Teleported.");
                        }
                        else {
                            final String targetServer = RaidCraft.getTable(WorldInfoTable.class).getWorldHost(world);
                            this.plugin.getTranslationProvider().msg(
                                    player,
                                    "command.teleport.player.serverchange",
                                    ChatColor.YELLOW,
                                    "Change to server: %1$s",
                                    targetServer);
                            plugin.getTeleportRequestManager()
                                    .addRequest(player.getName(),
                                            world, x, y, z, 0, 0);
                            BungeeCordUtil.changeServer(player, targetServer);
                        }
                        this.plugin.getTranslationProvider().msg(
                                sender,
                                "command.teleport.sender.success",
                                Colors.Chat.SUCCESS,
                                "You teleported %1$s.",
                                player.getName());
                        return;
                    }
                    catch(NumberFormatException | IndexOutOfBoundsException e) {
                        throw new CommandException(
                            this.plugin.getTranslationProvider().tr(
                                sender,
                                "command.teleport.sender.wrongFormat",
                                "Coordinates in the wrong format (x,y,z expected)"
                            )
                        );
                    }
                }

                // teleport to player
                MultiWorldPlayer multiWorldPlayer = plugin.getPlayerManager().getPlayer(context.getString(0));
                if(multiWorldPlayer == null || !multiWorldPlayer.isOnline()) {
                    throw new CommandException(
                        this.plugin.getTranslationProvider().tr(
                            sender,
                            "command.teleport.sender.noplayer",
                            "No matching players found."
                        )
                    );
                }

                Player targetPlayer = Bukkit.getPlayer(multiWorldPlayer.getName());
                if(targetPlayer != null) {
                    this.plugin.getPlayerManager().getPlayer(player.getName()).setBeforeTeleportLocation(new ServerLocation(player.getLocation()));
                    this.plugin.getBungeeManager().sendMessage(player, new SaveReturnLocationMessage(player.getName(), player.getLocation()));
                    player.teleport(targetPlayer);

                    this.plugin.getTranslationProvider().msg(
                        sender,
                        "command.teleport.sender.success",
                        Colors.Chat.SUCCESS,
                        "You teleported %1$s.",
                        player.getName()
                    );
                    this.plugin.getTranslationProvider().msg(
                        sender,
                        "command.teleport.player.success",
                        Colors.Chat.SUCCESS,
                        "Teleported."
                    );
                }
                else {
                    this.plugin.getPlayerManager().getPlayer(player.getName()).setBeforeTeleportLocation(new ServerLocation(player.getLocation()));
                    this.plugin.getBungeeManager().sendMessage(player, new SaveReturnLocationMessage(player.getName(), player.getLocation()));
                    FoundPlayersServerListener.registerAction(multiWorldPlayer.getName(), new TeleportOnServerFoundAction(player, this.plugin.getBungeeManager()));
                    this.plugin.getBungeeManager().sendMessage(player, new FindPlayersServerMessage(multiWorldPlayer.getName()));
                }
            }
        }
        // command block compatibility
        if(sender instanceof BlockCommandSender) {
            if(context.argsLength() == 4) {

                Player player = Bukkit.getPlayer(context.getString(0));
                if(player == null) return;
                String xS = context.getString(1);
                String yS = context.getString(2);
                String zS = context.getString(3);

                double x = getCommandBlockTeleportCoordinate(COORDINATE.X, player.getLocation().getX(), xS);
                double y = getCommandBlockTeleportCoordinate(COORDINATE.Y, player.getLocation().getY(), yS);
                double z = getCommandBlockTeleportCoordinate(COORDINATE.Z, player.getLocation().getZ(), zS);

                player.teleport(new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch()));
            }
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
                throw new CommandException(
                    this.plugin.getTranslationProvider().tr(
                            sender,
                            "command.teleport.sender.noplayer",
                            "No matching players found."
                    )
                );
            }

            Player targetPlayer = Bukkit.getPlayer(context.getString(0));
            if(targetPlayer != null) {
                plugin.getPlayerManager().getPlayer(targetPlayer.getName()).setBeforeTeleportLocation(new ServerLocation(targetPlayer.getLocation()));
                plugin.getBungeeManager().sendMessage(player, new SaveReturnLocationMessage(targetPlayer.getName(), targetPlayer.getLocation()));
                targetPlayer.teleport(player);
                this.plugin.getTranslationProvider().msg(
                        sender,
                        "command.teleport.sender.success",
                        Colors.Chat.SUCCESS,
                        "You teleported %1$s.",
                        player.getName()
                );
                this.plugin.getTranslationProvider().msg(
                        sender,
                        "command.teleport.player.success",
                        Colors.Chat.SUCCESS,
                        "Teleported."
                );
                return;
            }

            plugin.getTeleportRequestManager()
                    .addRequest(player.getName(),
                            player.getWorld().getName(), player.getLocation().getX(), player.getLocation().getY(),
                            player.getLocation().getZ(), player.getLocation().getPitch(), player.getLocation().getYaw());
            plugin.getBungeeManager().sendMessage(player, new ChangeServerMessage(multiWorldPlayer.getName(), Bukkit.getWorlds().get(0).getName()));

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
            throw new CommandException(
                this.plugin.getTranslationProvider().tr(
                    sender,
                    "command.return.sender.fail",
                    "Keine Return Location gespeichert!"
                )
            );
        }

        Player player = (Player)sender;
        Location location = serverLocation.getBukkitLocation();
        if(location != null) {
            player.teleport(location);
            this.plugin.getTranslationProvider().msg(
                sender,
                "command.teleport.player.success",
                Colors.Chat.SUCCESS,
                "Teleported."
            );
        }
        else {

            plugin.getTeleportRequestManager()
                    .addRequest(player.getName(), serverLocation.getServer(), serverLocation.getX(), serverLocation.getY(),
                            serverLocation.getZ(), serverLocation.getPitch(), serverLocation.getYaw());
            BungeeCordUtil.changeServer(player, serverLocation.getServer());
        }
    }

    private double getCommandBlockTeleportCoordinate(COORDINATE coordinate, double playerCoord, String coordString) throws NumberFormatException {

        double coord;
        try{
            coord = Double.valueOf(coordString);
            if(coordinate != COORDINATE.Y) {
                coord += 0.5;
            }
        }
        catch (NumberFormatException e) {
            if(!coordString.startsWith("~")) throw new NumberFormatException();
            try {
                coord = Double.parseDouble(coordString.substring(1));
                coord += playerCoord;
            } catch (NumberFormatException e1) {
                throw new NumberFormatException();
            }
        }
        return coord;
    }

    public enum COORDINATE {
        X,
        Y,
        Z;
    }
}
