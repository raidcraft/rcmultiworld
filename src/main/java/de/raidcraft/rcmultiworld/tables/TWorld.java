package de.raidcraft.rcmultiworld.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "multiworld_world")
public class TWorld {

    @Id
    private int id;
    private String alias;
    private String server;
    private UUID worldId;
    private String folder;
}
