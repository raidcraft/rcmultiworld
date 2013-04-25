package de.raidcraft.rcmultiworld.restart;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip Urban
 */
public class ShutdownInfoTask implements Runnable {

    private RestartManager manager;
    private List<Integer> thresholds = new ArrayList<>();

    public ShutdownInfoTask(RestartManager manager) {

        this.manager = manager;
        reload();
    }

    public void reload() {

        thresholds.clear();
        for(int threshold : manager.getConfig().restartInfoThresholds) {
            thresholds.add(threshold);
        }
    }

    public void run() {

        int diff = (int)(manager.getNextRestart()/1000) - (int)(System.currentTimeMillis()/1000);

        if(diff <= 0) {

            manager.restart();
            return;
        }

        if(thresholds.contains(diff)) {
            String info = ChatColor.GOLD + "** Neustart in ";
            if(diff >= 120) {
                info += (diff/60) + " Minuten";
            }
            else if(diff == 60) {
                info += "1 Minute";
            }
            else if(diff >= 10) {
                info += diff + " Sekunden";
            }
            else {

                info = ChatColor.GOLD + "** " + diff;
            }

            info += " **";
            Bukkit.broadcastMessage(info);
        }
    }
}
