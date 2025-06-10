package me.SuperRonanCraft.BetterRTP.player.rtp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RTP_PlayerInfo {

    boolean
            applyDelay,
            applyCooldown,
            checkCooldown,
            takeMoney,
            takeHunger;

    public RTP_PlayerInfo() {
        this(true, true, true, true, true);
    }

    public RTP_PlayerInfo(boolean applyDelay, boolean applyCooldown) {
        this(applyDelay, applyCooldown, true);
    }

    public RTP_PlayerInfo(boolean applyDelay, boolean applyCooldown, boolean checkCooldown) {
        this(applyDelay, applyCooldown, checkCooldown, true, true);
    }

    public RTP_PlayerInfo(boolean applyDelay,
                          boolean applyCooldown,
                          boolean checkCooldown,
                          boolean takeMoney,
                          boolean takeHunger) {
        this.applyDelay = applyDelay;
        this.applyCooldown = applyCooldown;
        this.checkCooldown = checkCooldown;
        this.takeMoney = takeMoney;
        this.takeHunger = takeHunger;
    }

    public enum RTP_PLAYERINFO_FLAG {
        NODELAY, NOCOOLDOWN, IGNORECOOLDOWN, IGNOREMONEY, IGNOREHUNGER
    }

}
