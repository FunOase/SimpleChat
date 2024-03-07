package com.rappytv.chat.commands;

import com.rappytv.chat.RyChat;
import com.rappytv.rylib.RyLib;
import com.rappytv.rylib.util.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chat extends Command<RyChat> {

    private static boolean enabled = true;

    public Chat(String name, RyChat plugin) {
        super(name, plugin);
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        if(sender instanceof Player player) {
            if (!player.hasPermission("rychat.manage")) {
                player.sendMessage(RyLib.get().i18n().translate("noPermission"));
                return;
            }
        }
        if(args.length < 1) {
            sender.sendMessage(plugin.i18n().translate("command.chat.invalidSubcommand"));
            return;
        }
        if(args[0].equalsIgnoreCase("on")) {
            if(!sender.hasPermission("rychat.manage.enable")) {
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
            if(!sender.hasPermission("rychat.manage.disable")) {
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
        } else if(args[0].equalsIgnoreCase("reload")) {
            if(!sender.hasPermission("rychat.reload")) {
                sender.sendMessage(RyLib.get().i18n().translate("noPermission"));
                return;
            }

            plugin.reloadConfig();
            sender.sendMessage(plugin.i18n().translate("command.chat.reloaded"));
        } else {
            sender.sendMessage(plugin.i18n().translate("command.chat.invalidSubcommand"));
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String prefix, String[] args) {
        if(args.length == 1) {
            List<String> list = new ArrayList<>();
            if(sender.hasPermission("rychat.manage")) list.addAll(Arrays.asList("on", "off"));
            if(sender.hasPermission("rychat.reload")) list.add("reload");
            return tab(args[0], list);
        }
        return null;
    }

    public static boolean isEnabled() {
        return enabled;
    }
}