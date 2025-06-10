package me.SuperRonanCraft.BetterRTP.references.depends;

import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldPlayer;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class DepEconomy {
    private Economy e;
    private int hunger = 0;
    private boolean checked = false;

    public boolean charge(CommandSender sendi, WorldPlayer pWorld) {
        check(false);
        Player player = pWorld.getPlayer();
        //Hunger Stuff
        boolean took_food = false;
        if (hunger != 0
                && pWorld.getPlayerInfo().isTakeHunger()
                && !PermissionNode.BYPASS_HUNGER.check(player)
                && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)) {
            boolean has_hunger = player.getFoodLevel() >= hunger;
            if (has_hunger) {
                player.setFoodLevel(player.getFoodLevel() - hunger);
                took_food = true;
            } else {
                MessagesCore.FAILED_HUNGER.send(sendi);
                return false;
            }
        }
        //Economy Stuff
        if (e != null
                && pWorld.getPrice() != 0
                && pWorld.getPlayerInfo().isTakeMoney()
                && !PermissionNode.BYPASS_ECONOMY.check(player)) {
            try {
                EconomyResponse r = e.withdrawPlayer(player, pWorld.getPrice());
                boolean passed_economy = r.transactionSuccess();
                if (passed_economy) return true;

                MessagesCore.FAILED_PRICE.send(sendi, pWorld.getPrice());
                if (took_food)
                    player.setFoodLevel(player.getFoodLevel() + hunger);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Default value
        return true;
    }

    public boolean hasBalance(WorldPlayer pWorld) {
        check(false);
        //Economy Stuff
        int price = pWorld.getPrice();
        if (e != null && price != 0 && !PermissionNode.BYPASS_ECONOMY.check(pWorld.getPlayer())) {
            try {
                return e.getBalance(pWorld.getPlayer()) >= price;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Default value
        return true;
    }

    public boolean hasHunger(WorldPlayer pWorld) {
        check(false);
        Player player = pWorld.getPlayer();
        //Hunger Stuff
        if (hunger != 0
                && !PermissionNode.BYPASS_HUNGER.check(player)
                && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)) {
            return player.getFoodLevel() >= hunger;
        }
        //Default value
        return true;
    }

    public void load() {
        check(true);
    }

    private void check(boolean force) {
        if (!checked || force)
            registerEconomy();
        if (BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.ECO).getBoolean("Hunger.Enabled"))
            hunger = BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.ECO).getInt("Hunger.Honches");
        else
            hunger = 0;
    }

    private void registerEconomy() {
        try {
            if (BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.ECO).getBoolean("Economy.Enabled"))
                if (BetterRTP.getInstance().getServer().getPluginManager().isPluginEnabled("Vault")) {
                    RegisteredServiceProvider<Economy> rsp = BetterRTP.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
                    assert rsp != null;
                    e = rsp.getProvider();
                }
        } catch (NullPointerException ignored) {
        }
        checked = true;
    }
}
