package me.SuperRonanCraft.BetterRTP.player.rtp;

import lombok.Getter;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;

@Getter
public enum RTP_ERROR_REQUEST_REASON {
    IS_RTPING(MessagesCore.ALREADY),
    NO_PERMISSION(MessagesCore.NOPERMISSION_WORLD),
    WORLD_DISABLED(MessagesCore.DISABLED_WORLD),
    COOLDOWN(MessagesCore.COOLDOWN),
    PRICE_ECONOMY(MessagesCore.FAILED_PRICE),
    PRICE_HUNGER(MessagesCore.FAILED_HUNGER);

    private final MessagesCore msg;

    RTP_ERROR_REQUEST_REASON(MessagesCore msg) {
        this.msg = msg;
    }
}
