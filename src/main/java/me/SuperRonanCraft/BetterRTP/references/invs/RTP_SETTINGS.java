package me.SuperRonanCraft.BetterRTP.references.invs;

import lombok.Getter;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther.FILETYPE;

public enum RTP_SETTINGS {
    BLACKLIST(   SETTINGS_TYPE.BOOLEAN, FILETYPE.CONFIG, "Template.Enabled",
            new Object[]{true, "Templates", "&7Toggle Templates system", "paper"});

    @Getter
    SETTINGS_TYPE type;
    @Getter
    FILETYPE filetype;
    String path;
    String[] condition = null;
    @Getter
    Object[] info; // = new Object[]{false}; //ENABLED, NAME, DESCRIPTION, ITEM

    RTP_SETTINGS(SETTINGS_TYPE type, FILETYPE filetype, String path, Object[] info) {
        this.type = type;
        this.filetype = filetype;
        this.path = path;
        this.info = info;
    }

    RTP_SETTINGS(SETTINGS_TYPE type, FILETYPE filetype, String[] arry, Object[] info) {
        this.type = type;
        this.filetype = filetype;
        this.path = null;
        this.info = info;
        //Condition
        this.condition = arry;
    }

    void setValue(Object value) {
        BetterRTP.getInstance().getFiles().getType(filetype).setValue(path, value);
    }

    public Object getValue() {
        String path = this.path;
        if (path == null && condition != null) {
            if (BetterRTP.getInstance().getFiles().getType(filetype).getBoolean(condition[0]))
                path = condition[1];
            else
                path = condition[2];
        }
        return getValuePath(path);
    }

    private Object getValuePath(String path) {
        if (path != null) {
            if (getType() == SETTINGS_TYPE.BOOLEAN)
                return BetterRTP.getInstance().getFiles().getType(filetype).getBoolean(path);
            else if (getType() == SETTINGS_TYPE.STRING)
                return BetterRTP.getInstance().getFiles().getType(filetype).getString(path);
            else if (getType() == SETTINGS_TYPE.INTEGER)
                return BetterRTP.getInstance().getFiles().getType(filetype).getInt(path);
        }
        return null;
    }

}