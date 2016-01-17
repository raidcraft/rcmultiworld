package de.raidcraft.rcmultiworld.players;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.reference.Colors;
import de.raidcraft.util.UUIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Philip
 */
public class PlayerManager {

    private Map<UUID, MultiWorldPlayer> players = new HashMap<>();

    public void join(Player player) {

        if (RaidCraft.getComponent(RCMultiWorldPlugin.class).getConfig().broadcastPlayerJoinQuit) {
            Bukkit.broadcastMessage(Colors.Chat.INFO + player.getName() + " hat sich eingeloggt.");
        }
        if (!players.containsKey(player)) {
            players.put(player.getUniqueId(), new MultiWorldPlayer(player.getUniqueId()));
        }
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
    }

    public void leave(String name) {

        UUID uuid = UUIDUtil.convertPlayer(name);
        if(uuid == null) {
            return;
        }
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
