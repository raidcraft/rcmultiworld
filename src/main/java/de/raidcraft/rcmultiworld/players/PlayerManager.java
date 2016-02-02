package de.raidcraft.rcmultiworld.players;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.reference.Colors;
import de.raidcraft.util.UUIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Philip
 */
public class PlayerManager {

    private Map<UUID, MultiWorldPlayer> players = new HashMap<>();

    public void updatePlayerList(String playerList) {

        List<String> newPlayers = new ArrayList<>(Arrays.asList(playerList.toLowerCase().split(", ")));
        Map<UUID, MultiWorldPlayer> oldPlayers = new HashMap<>(players);
        for(Map.Entry<UUID, MultiWorldPlayer> entry : players.entrySet()) {
            if(!entry.getValue().isOnline()) {
                oldPlayers.remove(entry.getKey());
                continue;
            }
            if(newPlayers.contains(UUIDUtil.getNameFromUUID(entry.getKey()).toLowerCase())) {
                newPlayers.remove(UUIDUtil.getNameFromUUID(entry.getKey()).toLowerCase());
                oldPlayers.remove(entry.getKey());
            }
        }

        for(String player : newPlayers) {
            join(player);
        }
        for(Map.Entry<UUID, MultiWorldPlayer> entry : oldPlayers.entrySet()) {
            if(!entry.getValue().isOnline()) continue;
            leave(entry.getKey());
        }
    }

    public void join(Player player) {

        if (RaidCraft.getComponent(RCMultiWorldPlugin.class).getConfig().broadcastPlayerJoinQuit) {
            Bukkit.broadcastMessage(Colors.Chat.INFO + player.getName() + " hat sich eingeloggt.");
        }
        if (!players.containsKey(player.getUniqueId())) {
            players.put(player.getUniqueId(), new MultiWorldPlayer(player.getUniqueId()));
        }
        players.get(player.getUniqueId()).setOnline(true);
    }

    public void join(String name) {

        UUID uuid = UUIDUtil.convertPlayer(name);
        if(uuid == null) {
            return;
        }
        players.put(uuid, new MultiWorldPlayer(uuid));
    }

    public void leave(Player player) {

        if (RaidCraft.getComponent(RCMultiWorldPlugin.class).getConfig().broadcastPlayerJoinQuit) {
            Bukkit.broadcastMessage(Colors.Chat.INFO + player.getName() + " hat das Spiel verlassen.");
        }
        players.get(player.getUniqueId()).setOnline(false);
        players.remove(player.getUniqueId());
    }

    public void leave(String name) {

        UUID uuid = UUIDUtil.convertPlayer(name);
        if(uuid == null) {
            return;
        }
        leave(uuid);
    }

    public void leave(UUID uuid) {

        players.remove(uuid);
    }

    public void clear() {
        players.clear();
    }

    public MultiWorldPlayer getPlayer(UUID player) {
        if (!players.containsKey(player)) {
            Player bukkitPlayer = Bukkit.getPlayer(player);
            if(bukkitPlayer != null) {
                join(bukkitPlayer);
            }
        }
        return players.get(player);
    }

    // TODO: move into raid-cratp api?
    public Optional<MultiWorldPlayer> getPlayerStartsWith(String playerStartingName) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase().startsWith(playerStartingName.toLowerCase())) {
                return Optional.of(getPlayer(player.getUniqueId()));
            }
        }
        return Optional.empty();
    }

}
