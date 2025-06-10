package me.SuperRonanCraft.BetterRTP.references.player.playerdata;

import lombok.Getter;
import lombok.Setter;
import me.SuperRonanCraft.BetterRTP.references.invs.RTP_INV_SETTINGS;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;

@Setter
@Getter
public class PlayerData_Menus {

    private Inventory inv;
    RTP_INV_SETTINGS invType;
    World invWorld;
    RTP_INV_SETTINGS invNextInv;

}
