package me.SuperRonanCraft.BetterRTP.references.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.player.playerdata.PlayerData;

public class DatabasePlayers extends SQLite {

    public DatabasePlayers() {
        super(DATABASE_TYPE.PLAYERS);
    }

    @Override
    public List<String> getTables() {
        List<String> list = new ArrayList<>();
        list.add("Players");
        return list;
    }

    public enum COLUMNS {
        UUID("uuid", "varchar(32) PRIMARY KEY"),
        //COOLDOWN DATA
        COUNT("count", "long"),
        LAST_COOLDOWN_DATE("last_rtp_date", "long"),
        //USES("uses", "integer"),
        ;

        public final String name;
        public final String type;

        COLUMNS(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    public void setupData(PlayerData data) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + tables.getFirst() + " WHERE " + COLUMNS.UUID.name + " = ?");
            ps.setString(1, data.player.getUniqueId().toString());

            rs = ps.executeQuery();
            if (rs.next()) {
                long count = rs.getLong(COLUMNS.COUNT.name);
                long time = rs.getLong(COLUMNS.LAST_COOLDOWN_DATE.name);
                data.setRtpCount(Math.toIntExact(count));
                data.setGlobalCooldown(time);
            }
        } catch (SQLException ex) {
            BetterRTP.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            close(ps, rs, conn);
        }
    }

    //Set a player Cooldown
    public void setData(PlayerData data) {
        String pre = "INSERT OR REPLACE INTO ";
        String sql = pre + tables.getFirst() + " ("
                + COLUMNS.UUID.name + ", "
                + COLUMNS.COUNT.name + ", "
                + COLUMNS.LAST_COOLDOWN_DATE.name + " "
                //+ COLUMNS.USES.name + " "
                + ") VALUES(?, ?, ?)";
        List<Object> params = new ArrayList<>() {{
            add(data.player.getUniqueId().toString());
            add(data.getRtpCount());
            add(data.getGlobalCooldown());
            //add(data.getUses());
        }};
        sqlUpdate(sql, params);
    }
}