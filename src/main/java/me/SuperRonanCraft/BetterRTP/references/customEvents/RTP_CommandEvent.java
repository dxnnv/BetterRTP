package me.SuperRonanCraft.BetterRTP.references.customEvents;

import lombok.Getter;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class RTP_CommandEvent extends RTPEvent implements Cancellable {

    boolean cancelled;
    @Getter
    CommandSender sendi;
    @Getter
    RTPCommand cmd;
    private static final HandlerList handler = new HandlerList();

    //Called before a command is executed
    public RTP_CommandEvent(CommandSender sendi, RTPCommand cmd) {
        this.sendi = sendi;
        this.cmd = cmd;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
