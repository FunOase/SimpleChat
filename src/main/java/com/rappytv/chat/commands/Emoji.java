package com.rappytv.chat.commands;

import com.rappytv.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("all")
public class Emoji implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String prefix, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Chat.prefix + "§cDie Konsole darf diesen Befehl nicht ausführen!");
            return false;
        }
        Player player = (Player) sender;

        if(!player.hasPermission("chat.emojis.toggle")) {
            player.sendMessage(Chat.prefix + "§cDazu hast du keine Rechte!");
            return false;
        }
        boolean active = !player.hasPermission("chat.emojis");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set chat.emojis " + active);

        String text = active ? "§aaktiviert" : "§cdeaktiviert";
        player.sendMessage(Chat.prefix + "Emojis sind nun " + text + "§7!");
        return true;
    }
}
