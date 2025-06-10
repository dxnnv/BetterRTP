package me.SuperRonanCraft.BetterRTP.player.events;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommandType;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import me.SuperRonanCraft.BetterRTP.references.messages.Message;
import me.SuperRonanCraft.BetterRTP.references.messages.Message_RTP;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

class Interact {

    private boolean enabled;
    private String title, coloredTitle;

    void load() {
        String pre = "Settings.";
        FileOther.FILETYPE file = BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.SIGNS);
        enabled = file.getBoolean(pre + "Enabled");
        title = file.getString(pre + "Title");
        coloredTitle = Message.color(title);
    }

    void event(PlayerInteractEvent e) {
        if (enabled && e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && isSign(e.getClickedBlock())) {
            Sign sign = (Sign) e.getClickedBlock().getState();
            if (sign.getLine(0).equals(coloredTitle)) {
                String command = sign.getLine(1).split(" ")[0];
                if (cmd(sign.getLines()).split(" ")[0].equalsIgnoreCase("") || cmd(sign.getLines()).split(" ")[0].equalsIgnoreCase("rtp")) {
                    action(e.getPlayer(), null);
                    return;
                } else
                    for (RTPCommandType cmd : RTPCommandType.values())
                        if (command.equalsIgnoreCase(cmd.name())) {
                            action(e.getPlayer(), cmd(sign.getLines()).split(" "));
                            return;
                        }
                Message_RTP.sms(e.getPlayer(), "&cError! &7Command &a"
                        + Arrays.toString(cmd(sign.getLines()).split(" ")) + "&7 does not exist! Defaulting command to /rtp!");
            }
        }
    }

    void createSign(SignChangeEvent e) {
        if (enabled && PermissionNode.SIGN_CREATE.check(e.getPlayer())) {
            String line = e.getLine(0);
            if (line != null && (line.equalsIgnoreCase(title) ||
                    line.equalsIgnoreCase("[RTP]"))) {
                e.setLine(0, coloredTitle != null ? coloredTitle : "[RTP]");
                MessagesCore.SIGN.send(e.getPlayer(), cmd(e.getLines()));
            }
        }
    }

    private void action(Player p, String[] line) {
        BetterRTP.getInstance().getCmd().commandExecuted(p, "rtp", line);
    }

    private static String cmd(String[] signArray) {
        String actions = "";
        for (int i = 1; i < signArray.length; i++) {
            String line = signArray[i];
            if (line != null && !line.isEmpty())
                if (actions.isEmpty())
                    actions = line;
                else
                    actions = actions.concat(" " + line);
        }
        return actions;
    }

    private static boolean isSign(Block block) {
        return block.getState() instanceof Sign;
    }
}
