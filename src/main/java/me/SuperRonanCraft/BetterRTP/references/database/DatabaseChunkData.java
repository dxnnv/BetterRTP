package me.SuperRonanCraft.BetterRTP.references.database;

import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;
import org.bukkit.Chunk;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.List;

public class DatabaseChunkData extends SQLite {

    public DatabaseChunkData() {
        super(DATABASE_TYPE.CHUNK_DATA);
    }

    @Override
    public List<String> getTables() {
        List<String> list = new ArrayList<>();
        list.add("ChunkData");
        return list;
    }

    public enum COLUMNS {
        ID("id", "integer PRIMARY KEY AUTOINCREMENT"),
        //Chunk Data
        WORLD("world", "varchar(32)"),
        X("x", "long"),
        Z("z", "long"),
        BIOME("biome", "string"),
        MAX_Y("max_y", "integer"),
        ;

        public final String name;
        public final String type;

        COLUMNS(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }


    public void addChunk(Chunk chunk, int maxy, Biome biome) {
        AsyncHandler.async(() -> {
            String pre = "INSERT OR REPLACE INTO ";
            String sql = pre + tables.getFirst() + " ("
                    + COLUMNS.WORLD.name + ", "
                    + COLUMNS.X.name + ", "
                    + COLUMNS.Z.name + ", "
                    + COLUMNS.BIOME.name + ", "
                    + COLUMNS.MAX_Y.name + " "
                    + ") VALUES(?, ?, ?, ?, ?)";
            List<Object> params = new ArrayList<>() {{
                add(chunk.getWorld().getName());
                add(chunk.getX());
                add(chunk.getZ());
                add(biome.name());
                add(maxy);
            }};
            sqlUpdate(sql, params);
        });
    }

}