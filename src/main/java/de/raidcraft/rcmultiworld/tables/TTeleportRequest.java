package de.raidcraft.rcmultiworld.tables;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: Philip Urban
 * Date: 28.10.13
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "rcmultiworld_tp_requests")
@Getter
@Setter
public class TTeleportRequest {

    @Id
    private int id;
    private String player;
    private String world;
    private int x;
    private int y;
    private int z;
    private int pitch;
    private int yaw;

    public Location getBukkitLocation() {

        return new Location(
                Bukkit.getWorld(this.world),
                (double) this.x / 100.,
                (double) this.y / 100.,
                (double) this.z / 100.,
                (float) this.yaw / 100F,
                (float) this.pitch / 100F
        );
    }
}
