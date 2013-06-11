package de.raidcraft.rcmultiworld;

import de.raidcraft.rcmultiworld.bungeecord.messages.*;
import de.raidcraft.util.BungeeCordUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Philip
 */
public class BungeeManager {

    public static final Pattern MESSAGE_NAME_PATTERN = Pattern.compile("^([\\d]+)#([\\w]+)#(.+)$");
    public static final String DELIMITER = "#@#";

    private final RCMultiWorldPlugin plugin;
    private final String bungeeChannel;
    private final Map<String, Class<? extends BungeeMessage>> registeredMessages = new HashMap<>();

    protected BungeeManager(RCMultiWorldPlugin plugin, String bungeeChannel) {

        this.plugin = plugin;
        this.bungeeChannel = bungeeChannel;
        registerInhouseMessages();

        // update player list frequently
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {

                if (Bukkit.getOnlinePlayers().length == 0) return;

                BungeeCordUtil.getPlayerList(Bukkit.getOnlinePlayers()[0]);
            }
        }, 20, plugin.getConfig().getUpdateInterval() * 20);
    }

    public String getBungeeChannel() {

        return bungeeChannel;
    }

    private void registerInhouseMessages() {

        registerBungeeMessage(ExecuteCommandMessage.class);
        registerBungeeMessage(BroadcastMessage.class);
        registerBungeeMessage(TeleportToCoordsMessage.class);
        registerBungeeMessage(TeleportToPlayerMessage.class);
        registerBungeeMessage(FindPlayersServerMessage.class);
        registerBungeeMessage(SendPlayersServerMessage.class);
        registerBungeeMessage(SaveReturnLocationMessage.class);
    }

    public void registerBungeeMessage(Class<? extends BungeeMessage> clazz) {

        if (clazz.isAnnotationPresent(MessageName.class)) {
            registeredMessages.put(clazz.getAnnotation(MessageName.class).value(), clazz);
        }
    }

    public void sendMessage(Player player, BungeeMessage bungeeMessage) {

        BungeeCordUtil.sendPluginMessage(player, bungeeChannel, bungeeMessage.getEncoded());
    }

    public void receiveMessage(String encoded) {

        Matcher matcher = MESSAGE_NAME_PATTERN.matcher(encoded);
        if (matcher.matches()) {
            long timestamp = Long.parseLong(matcher.group(1));
            // check for timeout
            if(System.currentTimeMillis() - timestamp > 3*1000) {
                return;
            }
            String messageName = matcher.group(2);
            String[] args = matcher.group(3).split(DELIMITER);
            if (registeredMessages.containsKey(messageName)) {
                Class<? extends BungeeMessage> aClass = registeredMessages.get(messageName);
                for (Constructor<?> constructor : aClass.getDeclaredConstructors()) {
                    if (constructor.getParameterTypes().length == args.length) {
                        boolean match = true;
                        for (Class<?> param : constructor.getParameterTypes()) {
                            if (!String.class.isAssignableFrom(param)) {
                                match = false;
                                break;
                            }
                        }
                        if (match) {
                            try {
                                BungeeMessage bungeeMessage = (BungeeMessage) constructor.newInstance(args);
                                bungeeMessage.process();
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
