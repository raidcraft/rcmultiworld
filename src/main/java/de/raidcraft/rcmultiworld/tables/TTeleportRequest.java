package de.raidcraft.rcmultiworld.tables;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Optional;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Philip Urban
 * Date: 28.10.13
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
@Entity
@Table(name = "rc_multiworld_tp_requests")
public class TTeleportRequest {

    @Id
    private int id;
    private UUID player;
    @ManyToOne
    private TWorld world;
    private int x;
    private int y;
    private int z;
    private int yaw;
    private int pitch;

    public Optional<Location> getLocation() {
        World world = Bukkit.getWorld(getWorld().getFolder());
        if (world == null) {
            return Optional.empty();
        }
        return Optional.of(new Location(world,
                (double) getX(),
                (double) getY(),
                (double) getZ(),
                (float) getYaw(),
                (float) getPitch()));
    }
}
