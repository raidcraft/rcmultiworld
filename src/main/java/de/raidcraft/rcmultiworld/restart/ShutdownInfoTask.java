package de.raidcraft.rcmultiworld.restart;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.language.TranslationProvider;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.reference.Colors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip Urban
 */
public class ShutdownInfoTask implements Runnable {

    private final RestartManager manager;
    private final List<Integer> thresholds = new ArrayList<>();

    public ShutdownInfoTask(final RestartManager manager) {

        this.manager = manager;
        init();
    }

    public void reload() {

        init();
    }

    private void init() {

        this.thresholds.clear();
        for (final int threshold : this.manager.getConfig().restartInfoThresholds) {
            this.thresholds.add(threshold);
        }
    }

    public void run() {

        if (this.manager.getNextRestart() < 0) {
            return;
        }

        final int diff = (int) (this.manager.getNextRestart() / 1000) - (int) (System.currentTimeMillis() / 1000);

        if (diff == 0) {
            this.manager.setNextRestart(-1);
            this.manager.restart();
            return;
        }

        final TranslationProvider translationProvider = RaidCraft.getComponent(RCMultiWorldPlugin.class).getTranslationProvider();

        if (this.thresholds.contains(diff)) {

            if(diff >= 60) {

                translationProvider.broadcastMessage(
                        "server.restart.timer.minutes",
                        "%1$s** Server will be restarted in %2$s minute(s) **",
                        Colors.Chat.INFO,
                        diff / 60
                );
            } else {

                translationProvider.broadcastMessage(
                        "server.restart.timer.seconds",
                        "%1$s** Server will be restarted in %2$s seconds **",
                        Colors.Chat.WARNING,
                        diff
                );
            }
        }
    }
}
