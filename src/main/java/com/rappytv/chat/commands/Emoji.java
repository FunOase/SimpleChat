package com.rappytv.chat.commands;

import com.rappytv.chat.ChatPlugin;
import com.rappytv.rylib.RyLib;
import com.rappytv.rylib.util.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Emoji extends Command<ChatPlugin> {

    public Emoji(String name, ChatPlugin plugin) {
        super(name, plugin);
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(RyLib.get().i18n().translate("onlyPlayer"));
            return;
        }

        if(!player.hasPermission("chat.emojis.toggle")) {
            player.sendMessage(RyLib.get().i18n().translate("noPermission"));
            return;
        }
        boolean active = !player.hasPermission("chat.emojis");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set chat.emojis " + active);

        String text = active
                ? plugin.i18n().translate("command.emoji.activated")
                : plugin.i18n().translate("command.emoji.deactivated");
        player.sendMessage(
                plugin.i18n().translate("command.emoji.success")
                        .replace("<state>", text)
        );
    }

    @Override
    public List<String> complete(CommandSender sender, String prefix, String[] args) {
        return null;
    }
}
