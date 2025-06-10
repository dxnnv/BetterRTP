package me.SuperRonanCraft.BetterRTP.player.rtp.effects;

import lombok.Getter;

@Getter
public class RTPEffects {

    final RTPEffect_Particles particles = new RTPEffect_Particles();
    final RTPEffect_Potions potions = new RTPEffect_Potions();
    final RTPEffect_Sounds sounds = new RTPEffect_Sounds();
    final RTPEffect_Titles titles = new RTPEffect_Titles();

    //public HashMap<Player, List<CompletableFuture<Chunk>>> playerLoads = new HashMap<>();

    public void load() {
        particles.load();
        potions.load();
        sounds.load();
        titles.load();
    }

}
