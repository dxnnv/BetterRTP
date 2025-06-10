package me.SuperRonanCraft.BetterRTP.player.commands;

import lombok.Getter;
import me.SuperRonanCraft.BetterRTP.player.commands.types.*;

@Getter
public enum RTPCommandType {
    BIOME(new CmdBiome()),
    EDIT(new CmdEdit()),
    HELP(new CmdHelp()),
    INFO(new CmdInfo()),
    LOCATION(new CmdLocation()),
    PLAYER(new CmdPlayer()),
    PLAYERSUDO(new CmdPlayerSudo()),
    QUEUE(new CmdQueue(), true),
    RELOAD(new CmdReload()),
    //SETTINGS(new CmdSettings(), true),
    TEST(new CmdTest(), true),
    VERSION(new CmdVersion()),
    WORLD(new CmdWorld()),
    DEV(new CmdDeveloper(), true),
    LOGGER(new CmdLogger(), true),
    ;

    private final RTPCommand cmd;
    private boolean debugOnly = false;

    RTPCommandType(RTPCommand cmd) {
        this.cmd = cmd;
    }

    RTPCommandType(RTPCommand cmd, boolean debugOnly) {
        this.cmd = cmd;
        this.debugOnly = debugOnly;
    }

}
