package com.rappytv.chat.commands;

import com.rappytv.chat.ChatPlugin;
import com.rappytv.rylib.RyLib;
import com.rappytv.rylib.util.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Chat extends Command<ChatPlugin> {

    private static boolean enabled = true;

    public Chat(String name, ChatPlugin plugin) {
        super(name, plugin);
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        if(sender instanceof Player player) {
            if (!player.hasPermission("chat.manage")) {
                player.sendMessage(RyLib.get().i18n().translate("noPermission"));
                return;
            }
        }
        if(args.length < 1 || !Arrays.asList("on", "off").contains(args[0])) {
            sender.sendMessage(
                    RyLib.get().i18n().translate("usage")
                            .replace("<usage>", plugin.i18n().translate("command.chat.usage"))
                            .replace("<cmd>", prefix)
            );
            return;
        }
        if(args[0].equalsIgnoreCase("on")) {
            if(!sender.hasPermission("chat.manage.enable")) {
                sender.sendMessage(RyLib.get().i18n().translate("noPermission"));
                return;
            }
            if(enabled) {
                sender.sendMessage(plugin.i18n().translate("command.chat.alreadyOn"));
                return;
            }

            enabled = true;
            sender.sendMessage(plugin.i18n().translate("command.chat.successOn"));
            for(Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage(plugin.i18n().translate("command.chat.announceOn"));
            }
        } else if(args[0].equalsIgnoreCase("off")) {
            if(!sender.hasPermission("chat.manage.disable")) {
                sender.sendMessage(RyLib.get().i18n().translate("noPermission"));
                return;
            }
            if(!enabled) {
                sender.sendMessage(plugin.i18n().translate("command.chat.alreadyOff"));
                return;
            }

            enabled = false;
            sender.sendMessage(plugin.i18n().translate("command.chat.successOff"));
            for(Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage(plugin.i18n().translate("command.chat.announceOff"));
            }
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String prefix, String[] args) {
        if(args.length == 1)
            return Arrays.asList("on", "off");
        return null;
    }

    public static boolean isEnabled() {
        return enabled;
    }
}