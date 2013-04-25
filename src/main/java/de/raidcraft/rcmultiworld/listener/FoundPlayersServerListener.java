package de.raidcraft.rcmultiworld.listener;

import de.raidcraft.rcmultiworld.events.FoundPlayersServerEvent;
import de.raidcraft.rcmultiworld.utilclasses.FoundPlayersServerAction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Philip
 */
public class FoundPlayersServerListener implements Listener {

    private static Map<String, FoundPlayersServerAction> registeredActions = new HashMap<>();

    public static void registerAction(String player, FoundPlayersServerAction action) {

        registeredActions.put(player, action);
    }

    @EventHandler
    public void onPlayersServerFound(FoundPlayersServerEvent event) {

        FoundPlayersServerAction action = registeredActions.get(event.getPlayer());
        if(action == null || action.isExpired()) return;

        action.process(event);
    }
}
