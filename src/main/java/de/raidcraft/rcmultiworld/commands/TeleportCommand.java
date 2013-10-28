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

    public TeleportCommand(RCMultiWorldPlugin module) {

    }

    @Command(
            aliases = {"tp", "teleport"},
            desc = "Teleport command",
            min = 1
    )
         @CommandPermissions("rcmultiworld.tp")
         public void teleport(CommandContext context, CommandSender sender) throws CommandException {

        if(sender instanceof Player) {
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
                            sender.sendMessage(ChatColor.YELLOW + "You teleported " + player.getName() + ".");
                            player.sendMessage(ChatColor.YELLOW + "Teleported.");
                        }
                        else {
                            String targetServer = RaidCraft.getTable(WorldInfoTable.class).getWorldHost(world);
                            player.sendMessage(ChatColor.YELLOW + "Change to server: " + targetServer);
                            plugin.getTeleportRequestManager()
                                    .addRequest(player.getName(),
                                            world, x, y, z, 0, 0);
                            BungeeCordUtil.changeServer(player, targetServer);
                        }
                        sender.sendMessage(ChatColor.YELLOW + "You teleported " + player.getName() + ".");
                        return;
                    }
                    catch(NumberFormatException | IndexOutOfBoundsException e) {
                        throw new CommandException("Die Koordinaten haben das falsche Format! ('x,y,z' erwartet)");
                    }
                }

                // teleport to player
                MultiWorldPlayer multiWorldPlayer = plugin.getPlayerManager().getPlayer(context.getString(0));
                if(multiWorldPlayer == null || !multiWorldPlayer.isOnline()) {
                    throw new CommandException("Kein passender Spieler gefunden.");
                }

                Player targetPlayer = Bukkit.getPlayer(multiWorldPlayer.getName());
                if(targetPlayer != null) {
                    plugin.getPlayerManager().getPlayer(player.getName()).setBeforeTeleportLocation(new ServerLocation(player.getLocation()));
                    plugin.getBungeeManager().sendMessage(player, new SaveReturnLocationMessage(player.getName(), player.getLocation()));
                    player.teleport(targetPlayer);
                    sender.sendMessage(ChatColor.YELLOW + "You teleported " + player.getName() + ".");
                    player.sendMessage(ChatColor.YELLOW + "Teleported.");
                }
                else {
                    plugin.getPlayerManager().getPlayer(player.getName()).setBeforeTeleportLocation(new ServerLocation(player.getLocation()));
                    plugin.getBungeeManager().sendMessage(player, new SaveReturnLocationMessage(player.getName(), player.getLocation()));
                    FoundPlayersServerListener.registerAction(multiWorldPlayer.getName(), new TeleportOnServerFoundAction(player, plugin.getBungeeManager()));
                    plugin.getBungeeManager().sendMessage(player, new FindPlayersServerMessage(multiWorldPlayer.getName()));
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
                throw new CommandException("Kein passender Spieler gefunden.");
            }

            Player targetPlayer = Bukkit.getPlayer(context.getString(0));
            if(targetPlayer != null) {
                plugin.getPlayerManager().getPlayer(targetPlayer.getName()).setBeforeTeleportLocation(new ServerLocation(targetPlayer.getLocation()));
                plugin.getBungeeManager().sendMessage(player, new SaveReturnLocationMessage(targetPlayer.getName(), targetPlayer.getLocation()));
                targetPlayer.teleport(player);
                sender.sendMessage(ChatColor.YELLOW + "You teleported " + targetPlayer.getName() + ".");
                targetPlayer.sendMessage(ChatColor.YELLOW + "Teleported.");
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
            throw new CommandException("Keine Return Location gespeichert!");
        }

        Player player = (Player)sender;
        Location location = serverLocation.getBukkitLocation();
        if(location != null) {
            player.teleport(location);
            player.sendMessage(ChatColor.YELLOW + "Teleported.");
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
