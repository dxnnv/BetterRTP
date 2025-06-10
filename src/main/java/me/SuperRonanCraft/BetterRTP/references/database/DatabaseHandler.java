package me.SuperRonanCraft.BetterRTP.references.database;

import lombok.Getter;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;

@Getter
public class DatabaseHandler {

    private final DatabasePlayers databasePlayers = new DatabasePlayers();
    private final DatabaseCooldowns databaseCooldowns = new DatabaseCooldowns();
    private final DatabaseQueue databaseQueue = new DatabaseQueue();
    private final DatabaseChunkData databaseChunks = new DatabaseChunkData();

    public void load() {
        AsyncHandler.async(() -> {
            databasePlayers.load();
            databaseCooldowns.load();
            databaseQueue.load();
            databaseChunks.load();
        });
    }

    public static DatabasePlayers getPlayers() {
        return BetterRTP.getInstance().getDatabaseHandler().getDatabasePlayers();
    }

    public static DatabaseCooldowns getCooldowns() {
        return BetterRTP.getInstance().getDatabaseHandler().getDatabaseCooldowns();
    }

    public static DatabaseQueue getQueue() {
        return BetterRTP.getInstance().getDatabaseHandler().getDatabaseQueue();
    }

    //public static DatabaseChunkData getChunks() {
    //    return BetterRTP.getInstance().getDatabaseHandler().getDatabaseChunks();
    //}

}
