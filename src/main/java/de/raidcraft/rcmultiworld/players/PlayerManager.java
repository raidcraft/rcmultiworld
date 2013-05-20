package de.raidcraft.rcmultiworld.players;

import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.*;

/**
 * @author Philip
 */
public class PlayerManager {

    private Map<String, MultiWorldPlayer> players = new HashMap<>();
    private RCMultiWorldPlugin plugin;

    public PlayerManager(RCMultiWorldPlugin plugin) {

        this.plugin = plugin;
    }

    public void updatePlayerList(String playerList) {

        List<String> newPlayers = new ArrayList<>(Arrays.asList(playerList.split(", ")));
        Map<String, MultiWorldPlayer> oldPlayers = new HashMap<>(players);
        for(Map.Entry<String, MultiWorldPlayer> entry : players.entrySet()) {
            if(!entry.getValue().isOnline()) {
                oldPlayers.remove(entry.getKey());
                continue;
            }
            if(newPlayers.contains(entry.getKey())) {
                newPlayers.remove(entry.getKey());
                oldPlayers.remove(entry.getKey());
            }
        }

        for(String player : newPlayers) {
            join(player);
        }
        for(Map.Entry<String, MultiWorldPlayer> entry : oldPlayers.entrySet()) {
            if(!entry.getValue().isOnline()) continue;
            leave(entry.getKey());
        }
    }

    public void join(String player) {

        Bukkit.broadcastMessage(ChatColor.YELLOW + player + " hat sich eingeloggt.");
        if(!players.containsKey(player)) {
            players.put(player, new MultiWorldPlayer(player));
        }
        players.get(player).setOnline(true);
    }

    public void leave(String player) {

        Bukkit.broadcastMessage(ChatColor.YELLOW + player + " hat das Spiel verlassen.");
        players.get(player).setOnline(false);
    }

    public void clear() {
        players.clear();
    }

    public boolean isOnline(String player) {

        MultiWorldPlayer multiWorldPlayer = getPlayer(player);
        if(multiWorldPlayer == null || !multiWorldPlayer.isOnline()) {
            return false;
        }
        return true;
    }

    public MultiWorldPlayer getPlayer(String player) {

        for(Map.Entry<String, MultiWorldPlayer> entry : players.entrySet()) {
            if(entry.getKey().toLowerCase().startsWith(player.toLowerCase())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public List<MultiWorldPlayer> getOnlinePlayers() {

        List<MultiWorldPlayer> onlinePlayers = new ArrayList<>();
        for(Map.Entry<String, MultiWorldPlayer> entry : players.entrySet()) {
            if(entry.getValue().isOnline()) {
                onlinePlayers.add(entry.getValue());
            }
        }
        return onlinePlayers;
    }
}
