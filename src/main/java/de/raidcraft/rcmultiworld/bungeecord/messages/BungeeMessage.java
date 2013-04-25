package de.raidcraft.rcmultiworld.bungeecord.messages;

/**
 * @author Philip
 */
public abstract class BungeeMessage {

    public final String getEncoded() {

        String messageName = getClass().getAnnotation(MessageName.class).value();
        return System.currentTimeMillis() + "#" + messageName + "#" + encode();
    }

    protected abstract String encode();

    public abstract void process();
}
