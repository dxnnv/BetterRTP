package me.SuperRonanCraft.BetterRTP.references.customEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RTPEvent extends Event {

    private static final HandlerList handler = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handler;
    }

    public static HandlerList getHandlerList() {
        return handler;
    }
}
