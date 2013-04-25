package de.raidcraft.rcmultiworld.restart;

import de.raidcraft.api.config.ConfigurationBase;
import de.raidcraft.api.config.Setting;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;

/**
 * @author Philip
 */
public class RestartConfig extends ConfigurationBase<RCMultiWorldPlugin> {

    public RestartConfig(RCMultiWorldPlugin plugin) {

        super(plugin, "restart.yml");
    }

    @Setting("global-restart-command") public String globalRestartCommand = "multicraft_restart";
    @Setting("local-restart-command") public String localRestartCommand = "server_restart";
    @Setting("default-restart-delay") public int restartDelay = 11;
    @Setting("restart-info-thresholds") public Integer[] restartInfoThresholds = new Integer[]
    {
            90*60,
            60*60,
            30*60,
            15*60,
            10*60,
            5*60,
            1*60,
            30,
            20,
            10,
            9,
            8,
            7,
            6,
            5,
            4,
            3,
            2,
            1
    };
}
