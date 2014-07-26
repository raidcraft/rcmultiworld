package de.raidcraft.rcmultiworld;

import de.raidcraft.rcmultiworld.bungeecord.messages.BroadcastMessage;
import de.raidcraft.rcmultiworld.bungeecord.messages.BungeeMessage;
import de.raidcraft.rcmultiworld.bungeecord.messages.ChangeServerMessage;
import de.raidcraft.rcmultiworld.bungeecord.messages.ExecuteCommandMessage;
import de.raidcraft.rcmultiworld.bungeecord.messages.FindPlayersServerMessage;
import de.raidcraft.rcmultiworld.bungeecord.messages.MessageName;
import de.raidcraft.rcmultiworld.bungeecord.messages.SaveReturnLocationMessage;
import de.raidcraft.rcmultiworld.bungeecord.messages.SendPlayersServerMessage;
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

    public static final String DELIMITER = "#@#";

    private static final int INTERVAL_MULTIPLICATION_FACTOR = 20;
    private static final Pattern MESSAGE_NAME_PATTERN = Pattern.compile("^([\\d]+)#([\\w]+)#(.+)$");


    private final String bungeeChannel;
    private final Map<String, Class<? extends BungeeMessage>> registeredMessages = new HashMap<>();

    protected BungeeManager(final RCMultiWorldPlugin plugin, final String bungeeChannel) {

        this.bungeeChannel = bungeeChannel;
        registerInhouseMessages();

        // update player list frequently
        Bukkit.getScheduler().runTaskTimer(
                plugin,
                () -> {

                    if (Bukkit.getOnlinePlayers().isEmpty()) {
                        return;
                    }

                    BungeeCordUtil.getPlayerList(Bukkit.getOnlinePlayers().iterator().next());
                },
                Math.max(1, plugin.getConfig().getUpdateInterval()) * INTERVAL_MULTIPLICATION_FACTOR,
                Math.max(1, plugin.getConfig().getUpdateInterval()) * INTERVAL_MULTIPLICATION_FACTOR
        );
    }

    public String getBungeeChannel() {

        return this.bungeeChannel;
    }

    private void registerInhouseMessages() {

        registerBungeeMessage(ExecuteCommandMessage.class);
        registerBungeeMessage(BroadcastMessage.class);
        registerBungeeMessage(FindPlayersServerMessage.class);
        registerBungeeMessage(SendPlayersServerMessage.class);
        registerBungeeMessage(SaveReturnLocationMessage.class);
        registerBungeeMessage(ChangeServerMessage.class);
    }

    public void registerBungeeMessage(final Class<? extends BungeeMessage> clazz) {

        if (clazz.isAnnotationPresent(MessageName.class)) {
            this.registeredMessages.put(clazz.getAnnotation(MessageName.class).value(), clazz);
        }
    }

    public void sendMessage(final Player player, final BungeeMessage bungeeMessage) {

        BungeeCordUtil.sendPluginMessage(player, this.bungeeChannel, bungeeMessage.getEncoded());
    }

    public void receiveMessage(final String encoded) {

        final Matcher matcher = MESSAGE_NAME_PATTERN.matcher(encoded);
        if (!matcher.matches()) {
            return;
        }

        final long timestamp = Long.parseLong(matcher.group(1));
        // check for timeout
        if ((System.currentTimeMillis() - timestamp) > (3 * 1000)) {
            return;
        }

        final String messageName = matcher.group(2);
        final String[] args = matcher.group(3).split(DELIMITER);
        if (!this.registeredMessages.containsKey(messageName)) {
            return;
        }

        final Class<? extends BungeeMessage> aClass = this.registeredMessages.get(messageName);
        for (final Constructor<?> constructor : aClass.getDeclaredConstructors()) {
            if (constructor.getParameterTypes().length == args.length) {
                boolean match = true;
                for (final Class<?> param : constructor.getParameterTypes()) {
                    if (!String.class.isAssignableFrom(param)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    try {
                        final BungeeMessage bungeeMessage = (BungeeMessage) constructor.newInstance(args);
                        bungeeMessage.process();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
