package com.rappytv.rychat.commands;

import com.rappytv.rychat.RyChat;
import com.rappytv.rylib.RyLib;
import com.rappytv.rylib.util.Command;
import com.rappytv.rylib.util.I18n;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ChatClear extends Command<RyChat> {

    public ChatClear(String name, RyChat plugin) {
        super(name, plugin);
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        if(!sender.hasPermission("rychat.clear")) {
            sender.sendMessage(RyLib.get().i18n().translate("noPermission"));
            return;
        }

        String staff = sender instanceof Player
                ? sender.getName()
                : plugin.i18n().translate("command.chatclear.console");
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!player.hasPermission("rychat.clear.bypass"))
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
