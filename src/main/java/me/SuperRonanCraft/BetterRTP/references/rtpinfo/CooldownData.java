package me.SuperRonanCraft.BetterRTP.references.rtpinfo;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CooldownData {

    private final UUID uuid;
    @Setter private Long time;

    public CooldownData(UUID uuid, Long time) {
        this.uuid = uuid;
        this.time = time;
    }
}
