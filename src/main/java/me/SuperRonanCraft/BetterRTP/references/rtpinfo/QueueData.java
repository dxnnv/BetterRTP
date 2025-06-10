package me.SuperRonanCraft.BetterRTP.references.rtpinfo;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;

@Getter
public class QueueData {

    final int database_id;
    @Setter Location location;
    final long generated;

    public QueueData(Location location, long generated, int database_id) {
        this.location = location;
        this.generated = generated;
        this.database_id = database_id;
    }

}
