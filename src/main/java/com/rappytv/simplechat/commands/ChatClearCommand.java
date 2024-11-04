package com.rappytv.simplechat.commands;

import com.rappytv.simplechat.SimpleChat;
import net.funoase.sahara.bukkit.i18n.I18n;
import net.funoase.sahara.bukkit.util.Command;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
        if(!sender.hasPermission("simplechat.manage.chat.clear")) {
            sender.sendMessage(deserializeTranslatable(sender, "sahara.errors.missing_permissions"));
            return;
        }

        Component lines = Component.empty();
        for(int i = 0; i < 1000; i++) {
            lines = lines.append(Component.newline());
        }

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!player.hasPermission("simplechat.manage.chat.clear.bypass"))
                player.sendMessage(lines);
            String staff = sender instanceof Player
                    ? sender.getName()
                    : I18n.translate(player, "simplechat.commands.clear.console");
            player.sendMessage(deserializeTranslatable(
                    sender,
                    "simplechat.commands.clear.broadcast",
                    Placeholder.unparsed("player", staff)
            ));
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String prefix, String[] args) {
        return null;
    }
}
