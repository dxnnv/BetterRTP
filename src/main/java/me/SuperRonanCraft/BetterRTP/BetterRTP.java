package me.SuperRonanCraft.BetterRTP;

import lombok.Getter;
import me.SuperRonanCraft.BetterRTP.player.PlayerInfo;
import me.SuperRonanCraft.BetterRTP.player.commands.Commands;
import me.SuperRonanCraft.BetterRTP.player.events.EventListener;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP;
import me.SuperRonanCraft.BetterRTP.references.Permissions;
import me.SuperRonanCraft.BetterRTP.references.RTPLogger;
import me.SuperRonanCraft.BetterRTP.references.WarningHandler;
import me.SuperRonanCraft.BetterRTP.references.database.DatabaseHandler;
import me.SuperRonanCraft.BetterRTP.references.depends.DepEconomy;
import me.SuperRonanCraft.BetterRTP.references.depends.DepPlaceholderAPI;
import me.SuperRonanCraft.BetterRTP.references.file.Files;
import me.SuperRonanCraft.BetterRTP.references.invs.RTPInventories;
import me.SuperRonanCraft.BetterRTP.references.messages.Message_RTP;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;
import me.SuperRonanCraft.BetterRTP.references.player.playerdata.PlayerDataManager;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.CooldownHandler;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.QueueHandler;
import me.SuperRonanCraft.BetterRTP.references.settings.Settings;
import me.SuperRonanCraft.BetterRTP.references.web.Metrics;
import me.SuperRonanCraft.BetterRTP.references.web.Updater;
import me.SuperRonanCraft.BetterRTP.versions.FoliaHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Level;

public class BetterRTP extends JavaPlugin {
    @Getter private final Permissions perms = new Permissions();
    @Getter private final DepEconomy eco = new DepEconomy();
    @Getter private final Commands cmd = new Commands(this);
    @Getter private final RTP RTP = new RTP();
    private final EventListener listener = new EventListener();
    @Getter private static BetterRTP instance;
    @Getter private final Files files = new Files();
    @Getter private final RTPInventories invs = new RTPInventories();
    @Getter private final PlayerInfo pInfo = new PlayerInfo();
    @Getter private final PlayerDataManager playerDataManager = new PlayerDataManager();
    @Getter private final Settings settings = new Settings();
    @Getter private final CooldownHandler cooldowns = new CooldownHandler();
    @Getter private final QueueHandler queue = new QueueHandler();
    @Getter private final DatabaseHandler databaseHandler = new DatabaseHandler();
    @Getter private final WarningHandler warningHandler = new WarningHandler();
    @Getter private boolean PlaceholderAPI;
    @Getter private final RTPLogger rtpLogger = new RTPLogger();
    @Getter private final FoliaHandler foliaHandler = new FoliaHandler();

    @Override
    public void onEnable() {
        instance = this;
        registerDependencies();
        loadAll();
        new Updater(this);
        new Metrics(this);
        listener.registerEvents(this);
        queue.registerEvents(this);
        try {
            new DepPlaceholderAPI().register();
        } catch (NoClassDefFoundError ignored) {
        }
    }

    @Override
    public void onDisable() {
        invs.closeAll();
        queue.unload();
        rtpLogger.unload();
    }

    private void registerDependencies() {
        PlaceholderAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sendi, @NotNull Command cmd, @NotNull String label, String[] args) {
        try {
            this.cmd.commandExecuted(sendi, label, args);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Message_RTP.sms(sendi, "&cERROR &7Seems like your Administrator did not update their language file!");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return this.cmd.onTabComplete(sender, args);
    }

    public void reload(CommandSender sendi) {
        invs.closeAll();
        loadAll();
        MessagesCore.RELOAD.send(sendi);
    }

    //(Re)Load all plugin systems/files/cache
    private void loadAll() {
        foliaHandler.load();
        playerDataManager.clear();
        files.loadAll();
        settings.load();
        cooldowns.load();
        databaseHandler.load();
        rtpLogger.setup(this);
        invs.load();
        RTP.load();
        cmd.load();
        listener.load();
        eco.load();
        perms.register();
        queue.load();
    }

    public static void debug(String str) {
        if (instance.getSettings().isDebug())
            instance.getLogger().log(Level.FINEST, str);
    }
}
