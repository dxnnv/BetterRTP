package me.SuperRonanCraft.BetterRTP.player.rtp;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldLocation;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public class RTPSetupInformation {
    //Will provide information to setup an RTP attempt
    @Setter private World world;
    @NonNull private final CommandSender sender;
    @Nullable private final Player player;
    private final boolean personalized;
    @Setter @Nullable private List<String> biomes;
    @Setter @Nullable private WorldLocation location;
    @Nullable private final RTP_TYPE rtp_type;
    private final RTP_PlayerInfo playerInfo;

    public RTPSetupInformation(@Nullable World world,
                               @NonNull CommandSender sender,
                               @Nullable Player player,
                               boolean personalized) {
        this(world, sender, player, personalized, null, false, null, null);
    }

    public RTPSetupInformation(@Nullable World world,
                               @NonNull CommandSender sender,
                               @Nullable Player player,
                               boolean personalized,
                               @Nullable List<String> biomes,
                                boolean delay,
                               @Nullable RTP_TYPE rtp_type,
                               @Nullable WorldLocation location) {
        this(world, sender, player, personalized, biomes, delay, rtp_type, location, true);
    }

    public RTPSetupInformation(@Nullable World world,
                               @NonNull CommandSender sender,
                               @Nullable Player player,
                               boolean personalized,
                               @Nullable List<String> biomes,
                               boolean delay,
                               @Nullable RTP_TYPE rtp_type,
                               @Nullable WorldLocation location,
                               boolean cooldown) {
        this(world, sender, player, personalized, biomes, rtp_type, location, new RTP_PlayerInfo(delay, cooldown));
    }

    public RTPSetupInformation(@Nullable World world,
                               @NonNull CommandSender sender,
                               @Nullable Player player,
                               boolean personalized,
                               @Nullable List<String> biomes,
                               @Nullable RTP_TYPE rtp_type,
                               @Nullable WorldLocation location,
                               RTP_PlayerInfo playerInfo) {
        this.world = world;
        this.sender = sender;
        this.player = player;
        this.personalized = personalized;
        this.biomes = biomes;
        this.rtp_type = rtp_type;
        this.location = location;
        if (this.world == null) {
            if (player != null)
                this.world = player.getWorld();
        }
        this.playerInfo = playerInfo;
    }
}
