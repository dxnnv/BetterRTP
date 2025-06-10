package me.SuperRonanCraft.BetterRTP.references.rtpinfo;

import io.papermc.lib.PaperLib;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_SHAPE;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.RTPWorld;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WORLD_TYPE;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class RandomLocation {

    public static Location generateLocation(RTPWorld rtpWorld) {
        if (rtpWorld.getShape() == RTP_SHAPE.CIRCLE)
            return  generateRound(rtpWorld);
        else
            return generateSquare(rtpWorld);
    }

    private static Location generateSquare(RTPWorld rtpWorld) {
        //Generate a random X and Z based off the quadrant selected
        int min = rtpWorld.getMinRadius();
        int max = rtpWorld.getMaxRadius() - min;
        int x, z;
        int quadrant = new Random().nextInt(4);
        try {
            z = switch (quadrant) {
                case 0 -> {
                    x = new Random().nextInt(max) + min;
                    yield new Random().nextInt(max) + min;
                }
                case 1 -> {
                    x = -new Random().nextInt(max) - min;
                    yield -(new Random().nextInt(max) + min);
                }
                case 2 -> {
                    x = -new Random().nextInt(max) - min;
                    yield new Random().nextInt(max) + min;
                }
                default -> {
                    x = new Random().nextInt(max) + min;
                    yield -(new Random().nextInt(max) + min);
                }
            };
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            BetterRTP.getInstance().getLogger().warning("A bounding location was negative! Please check your config only has positive x/z for max/min radius!");
            BetterRTP.getInstance().getLogger().warning("Max: " + rtpWorld.getMaxRadius() + " Min: " + rtpWorld.getMinRadius());
            return null;
        }
        x += rtpWorld.getCenterX();
        z += rtpWorld.getCenterZ();
        //System.out.println(quadrant);
        return new Location(rtpWorld.getWorld(), x, 69, z);
    }

    private static Location generateRound(RTPWorld rtpWorld) {
        //Generate a random X and Z based off location on a spiral curve
        int min = rtpWorld.getMinRadius();
        int max = rtpWorld.getMaxRadius() - min;
        int x, z;

        double area = Math.PI * (max - min) * (max + min); //of all the area in this donut
        double subArea = area * new Random().nextDouble(); //pick a random subset of that area

        double r = Math.sqrt(subArea/Math.PI + min * min); //convert area to radius
        double theta = (r - (int) r) * 2 * Math.PI; //use the remainder as an angle

        // polar to cartesian
        x = (int) (r * Math.cos(theta));
        z = (int) (r * Math.sin(theta));
        x += rtpWorld.getCenterX();
        z += rtpWorld.getCenterZ();
        return new Location(rtpWorld.getWorld(), x, 69, z);
    }

    public static Location getSafeLocation(WORLD_TYPE type, World world, Location loc, int minY, int maxY, List<String> biomes) {
        if (type == WORLD_TYPE.NETHER) //Get a Y position and check for bad blocks
            return getLocAtNether(loc.getBlockX(), loc.getBlockZ(), minY, maxY, world, biomes);
        else
            return getLocAtNormal(loc.getBlockX(), loc.getBlockZ(), minY, maxY, world, biomes);
    }
    private static Location getLocAtNormal(int x, int z, int minY, int maxY, World world, List<String> biomes) {
        Block b = getHighestBlock(x, z, world);
        if (!b.getType().isSolid()) { //Water, lava, shrubs...
            if (!badBlock(b.getType().name(), x, z, world, null)) { //Make sure it's not an invalid block (ex: water, lava...)
                //int y = world.getHighestBlockYAt(x, z);
                b = world.getBlockAt(x, b.getY() - 1, z);
            }
        }
        //Between max and min y
        if (    b.getY() >= minY
                && b.getY() <= maxY
                && !badBlock(b.getType().name(), x, z, world, biomes)) {
            return new Location(world, x, b.getY() + 1, z);
        }
        return null;
    }

    public static Block getHighestBlock(int x, int z, World world) {
        Block b = world.getHighestBlockAt(x, z);
        if (b.getType().toString().endsWith("AIR")) //1.15.1 or less
            b = world.getBlockAt(x, b.getY() - 1, z);
        return b;
    }

    private static Location getLocAtNether(int x, int z, int minY, int maxY, World world, List<String> biomes) {
        //Max and Min Y
        for (int y = minY + 1; y < maxY/*world.getMaxHeight()*/; y++) {
            Block block_current = world.getBlockAt(x, y, z);
            Material blockType = block_current.getType();
            if (blockType.name().endsWith("AIR") || !blockType.isSolid()) {
                if (!blockType.name().endsWith("AIR") && !blockType.isSolid()) { //Block is not a solid (ex: lava, water...)
                    String block_in = blockType.name();
                    if (badBlock(block_in, x, z, world, null))
                        continue;
                }
                String block = world.getBlockAt(x, y - 1, z).getType().name();
                if (block.endsWith("AIR")) //Block below is air, skip
                    continue;
                if (world.getBlockAt(x, y + 1, z).getType().name().endsWith("AIR") //Headspace
                        && !badBlock(block, x, z, world, biomes)) //Valid block
                    return new Location(world, x, y, z);
            }
        }
        return null;
    }

    // Bad blocks, or bad biome
    public static boolean badBlock(String block, int x, int z, World world, List<String> biomes) {
        for (String currentBlock : BetterRTP.getInstance().getRTP().getBlockList()) //Check Block
            if (currentBlock.equalsIgnoreCase(block))
                return true;
        //Check Biomes
        if (biomes == null || biomes.isEmpty())
            return false;
        String biomeCurrent = world.getBiome(x, z).name();
        for (String biome : biomes)
            if (biomeCurrent.toUpperCase().contains(biome.toUpperCase()))
                return false;
        return true;
        //FALSE MEANS NO BAD BLOCKS/BIOME WHERE FOUND!
    }

    public static void runChunkTest() {
        BetterRTP.getInstance().getLogger().info("---------------- Starting chunk test!");
        World world = Bukkit.getWorld("world");
        cacheChunkAt(world, 32, -32, -32, -32);
    }

    private static void cacheTask(World world, int goal, int start, int xat, int zat) {
        zat += 1;
        if (zat > goal) {
            zat = start;
            xat += 1;
        }
        if (xat <= goal)
            cacheChunkAt(world, goal, start, xat, zat);
    }

    private static void cacheChunkAt(World world, int goal, int start, int xat, int zat) {
        CompletableFuture<Chunk> task = PaperLib.getChunkAtAsync(new Location(world, xat * 16, 0, zat * 16));
        task.thenAccept(chunk -> {
            try {
                ChunkSnapshot snapshot = chunk.getChunkSnapshot(true, true, false);
                int maxy = snapshot.getHighestBlockYAt(8, 8);
                Biome biome = snapshot.getBiome(8, 8);
                //BetterRTP.getInstance().getLogger().info("Added " + chunk.getX() + " " + chunk.getZ());
                BetterRTP.getInstance().getDatabaseHandler().getDatabaseChunks().addChunk(chunk, maxy, biome);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException();
                //BetterRTP.getInstance().getLogger().info("Tried Adding " + chunk.getX() + " " + chunk.getZ());
            }
        }).thenRun(() -> cacheTask(world, goal, start, xat, zat));
    }

}
