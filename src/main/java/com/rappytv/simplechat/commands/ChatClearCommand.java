package com.rappytv.simplechat.commands;

import com.rappytv.simplechat.SimpleChat;
import net.funoase.sahara.bukkit.util.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ChatClearCommand extends Command<SimpleChat> {

    public ChatClearCommand(String name, SimpleChat plugin) {
        super(name, plugin);
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        if(!sender.hasPermission("simplechat.clear")) {
            sender.sendMessage(RyLib.get().i18n().translate("noPermission"));
            return;
        }

        String staff = sender instanceof Player
                ? sender.getName()
                : plugin.i18n().translate("command.chatclear.console");
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!player.hasPermission("simplechat.clear.bypass"))
                player.sendMessage("§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n");
            player.sendMessage(
                    plugin.i18n().translate(
                            "command.chatclear.success",
                            new I18n.Argument("staff", staff)
                    )
            );
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String prefix, String[] args) {
        return null;
    }
}
