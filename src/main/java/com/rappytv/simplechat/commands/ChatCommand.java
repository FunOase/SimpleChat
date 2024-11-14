package com.rappytv.simplechat.commands;

import com.rappytv.simplechat.SimpleChat;
import net.funoase.sahara.bukkit.i18n.I18n;
import net.funoase.sahara.bukkit.util.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatCommand extends Command<SimpleChat> {

    private static boolean enabled = true;

    public ChatCommand(String name, SimpleChat plugin) {
        super(name, plugin);
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        if (!sender.hasPermission("simplechat.manage.command")) {
            sender.sendMessage(I18n.component(sender, "sahara.errors.missing_permissions", true));
            return;
        }
        switch (args.length > 0 ? args[0].toLowerCase() : "") {
            case "enable" -> {
                if(!sender.hasPermission("simplechat.manage.chat.toggle")) {
                    sender.sendMessage(I18n.component(sender, "sahara.errors.missing_permissions", true));
                    return;
                }
                if(enabled) {
                    sender.sendMessage(I18n.component(sender, "simplechat.commands.chat.enable.already", true));
                    return;
                }

                enabled = true;
                for(Player all : Bukkit.getOnlinePlayers()) {
                    all.sendMessage(I18n.component(sender, "simplechat.commands.chat.enable.broadcast", true));
                }
            }
            case "disable" -> {
                if(!sender.hasPermission("simplechat.manage.chat.toggle")) {
                    sender.sendMessage(I18n.component(sender, "sahara.errors.missing_permissions", true));
                    return;
                }
                if(!enabled) {
                    sender.sendMessage(I18n.component(sender, "simplechat.commands.chat.disable.already", true));
                    return;
                }

                enabled = false;
                for(Player all : Bukkit.getOnlinePlayers()) {
                    all.sendMessage(I18n.component(sender, "simplechat.commands.chat.disable.broadcast", true));
                }
            }
            case "reload" -> {
                if(!sender.hasPermission("simplechat.manage.reload")) {
                    sender.sendMessage(I18n.component(sender, "sahara.errors.missing_permissions", true));
                    return;
                }

                plugin.reloadConfig();
                sender.sendMessage(I18n.component(sender, "simplechat.commands.chat.reload.success", true));
            }
            default -> sender.sendMessage(I18n.component(sender, "simplechat.commands.chat.invalid_subcommand", true));
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String prefix, String[] args) {
        if(args.length == 1) {
            List<String> list = new ArrayList<>();
            if(sender.hasPermission("simplechat.manage.chat.toggle")) list.addAll(Arrays.asList("enable", "disable"));
            if(sender.hasPermission("simplechat.manage.reload")) list.add("reload");
            return tab(args[0], list);
        }
        return null;
    }

    public static boolean isEnabled() {
        return enabled;
    }
}