package de.raidcraft.rcmultiworld.players;

import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.reference.Colors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Philip
 */
public class PlayerManager {

    private final RCMultiWorldPlugin plugin;
    private final Map<String, MultiWorldPlayer> players = new HashMap<>();

    public PlayerManager(final RCMultiWorldPlugin plugin) {

        this.plugin = plugin;
    }

    public void updatePlayerList(final String playerList) {

        final List<String> newPlayers = new ArrayList<>(Arrays.asList(playerList.split(", ")));
        final Map<String, MultiWorldPlayer> oldPlayers = new HashMap<>(this.players);
        for (final Map.Entry<String, MultiWorldPlayer> entry : this.players.entrySet()) {
            if (!entry.getValue().isOnline()) {
                oldPlayers.remove(entry.getKey());
                continue;
            }
            if (newPlayers.contains(entry.getKey())) {
                newPlayers.remove(entry.getKey());
                oldPlayers.remove(entry.getKey());
            }
        }

        newPlayers.forEach(this::join);
        oldPlayers.entrySet().stream().filter(entry -> entry.getValue().isOnline()).forEach(entry -> leave(entry.getKey()));
    }

    public void join(final String player) {

        this.plugin.getTranslationProvider().broadcastMessage(
                "player.login.broadcast",
                Colors.Chat.INFO,
                "Player %1$s joined the game.",
                player
        );
        if (!this.players.containsKey(player)) {
            this.players.put(player, new MultiWorldPlayer(player));
        }
        this.players.get(player).setOnline(true);
    }

    public void leave(final String player) {

        this.plugin.getTranslationProvider().broadcastMessage(
                "player.logout.broadcast",
                Colors.Chat.INFO,
                "Player %1$s left the game.",
                player
        );
        // TODO: Für was werden offline Spieler benötigt?
        this.players.get(player).setOnline(false);
    }

    public void clear() {

        this.players.clear();
    }

    public boolean isOnline(final String player) {

        final MultiWorldPlayer multiWorldPlayer = getPlayer(player);
        return (multiWorldPlayer != null) && multiWorldPlayer.isOnline();
    }

    public MultiWorldPlayer getPlayer(final String player) {

        for (final Map.Entry<String, MultiWorldPlayer> entry : this.players.entrySet()) {
            if (entry.getKey().toLowerCase().startsWith(player.toLowerCase())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public List<MultiWorldPlayer> getOnlinePlayers() {

        return this.players.entrySet().stream()
                .filter(entry -> entry.getValue().isOnline())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
