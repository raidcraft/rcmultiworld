package de.raidcraft.rcmultiworld.players;

import de.raidcraft.reference.Colors;
import de.raidcraft.util.UUIDUtil;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Philip
 */
public class PlayerManager {

    private Map<UUID, MultiWorldPlayer> players = new HashMap<>();

    public void join(UUID player) {
        Bukkit.broadcastMessage(Colors.Chat.INFO + UUIDUtil.getNameFromUUID(player) + " hat sich eingeloggt.");
        if (!players.containsKey(player)) {
            players.put(player, new MultiWorldPlayer(player));
        }
    }

    public void leave(UUID player) {
        Bukkit.broadcastMessage(Colors.Chat.INFO + UUIDUtil.getNameFromUUID(player) + " hat das Spiel verlassen.");
    }

    public void clear() {
        players.clear();
    }

    public MultiWorldPlayer getPlayer(UUID player) {
        if (!players.containsKey(player)) {
            join(player);
        }
        return players.get(player);
    }
}
