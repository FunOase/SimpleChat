package com.rappytv.chat.commands;

import com.rappytv.chat.ChatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("all")
public class Chat implements CommandExecutor, TabCompleter {

    private static boolean enabled = true;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String prefix, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("chat.manage.enable") && !p.hasPermission("chat.manage.disable")) {
                p.sendMessage(ChatPlugin.prefix + "§cDazu hast du keine Berechtigung!");
                return false;
            }
        }
        if(args.length < 1 || !Arrays.asList("on", "off").contains(args[0])) {
            sender.sendMessage(ChatPlugin.prefix + "§cSo benutzt du den Befehl: §b/" + prefix + " <on|off>§c!");
            return false;
        }
        if(args[0].equalsIgnoreCase("on")) {
            if(!sender.hasPermission("chat.manage.enable")) {
                sender.sendMessage(ChatPlugin.prefix + "§cDazu hast du keine Berechtigung!");
                return false;
            }
            if(enabled) {
                sender.sendMessage(ChatPlugin.prefix + "§cDer Chat ist bereits aktiviert!");
                return false;
            }

            enabled = true;
            sender.sendMessage(ChatPlugin.prefix + "Der Chat wurde erfolgreich §baktiviert§7!");
            for(Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage(ChatPlugin.prefix + "Der Chat wurde §baktiviert§7!");
            }
        } else if(args[0].equalsIgnoreCase("off")) {
            if(!sender.hasPermission("chat.manage.disable")) {
                sender.sendMessage(ChatPlugin.prefix + "§cDazu hast du keine Berechtigung!");
                return false;
            }
            if(!enabled) {
                sender.sendMessage(ChatPlugin.prefix + "§cDer Chat ist bereits deaktiviert!");
                return false;
            }

            enabled = false;
            sender.sendMessage(ChatPlugin.prefix + "Der Chat wurde erfolgreich §bdeaktiviert§7!");
            for(Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage(ChatPlugin.prefix + "Der Chat wurde §bdeaktiviert§7!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1)
            return new ArrayList<>(Arrays.asList("on", "off"));
        return Collections.emptyList();
    }

    public static boolean isEnabled() {
        return enabled;
    }
}