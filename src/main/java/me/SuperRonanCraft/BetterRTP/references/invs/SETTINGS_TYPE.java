package me.SuperRonanCraft.BetterRTP.references.invs;

import java.io.Serializable;

public enum SETTINGS_TYPE {
    BOOLEAN(Boolean.class), STRING(String.class), INTEGER(Integer.class);

    private Serializable type;

    SETTINGS_TYPE(Serializable type) {
        this.type = type;
    }

    Serializable getType() {
        return type;
    }
}