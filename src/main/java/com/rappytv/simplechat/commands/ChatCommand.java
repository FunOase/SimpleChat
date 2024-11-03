package com.rappytv.simplechat.commands;

import com.rappytv.simplechat.SimpleChat;
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
        if (!sender.hasPermission("simplechat.manage")) {
            sender.sendMessage(deserializeTranslatable(sender, "sahara.errors.missing_permissions"));
            return;
        }
        switch (args.length > 0 ? args[0].toLowerCase() : "") {
            case "enable" -> {
                if(!sender.hasPermission("simplechat.chat.manage.toggle")) {
                    sender.sendMessage(deserializeTranslatable(sender, "sahara.errors.missing_permissions"));
                    return;
                }
                if(enabled) {
                    sender.sendMessage(deserializeTranslatable(sender, "simplechat.commands.chat.enable.already"));
                    return;
                }

                enabled = true;
                for(Player all : Bukkit.getOnlinePlayers()) {
                    all.sendMessage(deserializeTranslatable(sender, "simplechat.commands.chat.enable.broadcast"));
                }
            }
            case "disable" -> {
                if(!sender.hasPermission("simplechat.chat.manage.toggle")) {
                    sender.sendMessage(deserializeTranslatable(sender, "sahara.errors.missing_permissions"));
                    return;
                }
                if(!enabled) {
                    sender.sendMessage(deserializeTranslatable(sender, "simplechat.commands.chat.disable.already"));
                    return;
                }

                enabled = false;
                for(Player all : Bukkit.getOnlinePlayers()) {
                    all.sendMessage(deserializeTranslatable(sender, "simplechat.commands.chat.disable.already"));
                }
            }
            case "reload" -> {
                if(!sender.hasPermission("simplechat.reload")) {
                    sender.sendMessage(deserializeTranslatable(sender, "sahara.errors.missing_permissions"));
                    return;
                }

                plugin.reloadConfig();
                sender.sendMessage(deserializeTranslatable(sender, "simplechat.commands.chat.reload.success"));
            }
            default -> sender.sendMessage(deserializeTranslatable(sender, "simplechat.commands.chat.invalid_subcommand"));
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String prefix, String[] args) {
        if(args.length == 1) {
            List<String> list = new ArrayList<>();
            if(sender.hasPermission("simplechat.chat.manage.toggle")) list.addAll(Arrays.asList("enable", "disable"));
            if(sender.hasPermission("simplechat.reload")) list.add("reload");
            return tab(args[0], list);
        }
        return null;
    }

    public static boolean isEnabled() {
        return enabled;
    }
}