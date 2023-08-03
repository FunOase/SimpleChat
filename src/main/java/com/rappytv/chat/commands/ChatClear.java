package com.rappytv.chat.commands;

import com.rappytv.chat.ChatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ChatClear implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String prefix, String[] args) {
        if(!sender.hasPermission("chat.clear")) {
            sender.sendMessage(ChatPlugin.prefix + "§cDazu hast du keine Berechtigung!");
            return false;
        }

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!player.hasPermission("chat.clear.bypass"))
                player.sendMessage("§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n§e\n");
            player.sendMessage(ChatPlugin.prefix + "Der Chat wurde gerade von §b" + (sender instanceof Player ? sender.getName() : "Konsole") + " §7gecleart!");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String prefix, String[] args) {
        return Collections.emptyList();
    }
}
