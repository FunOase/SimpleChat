package com.rappytv.simplechat.commands;

import com.rappytv.simplechat.SimpleChat;
import net.funoase.sahara.bukkit.i18n.I18n;
import net.funoase.sahara.bukkit.util.Command;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class EmojiCommand extends Command<SimpleChat> {

    public EmojiCommand(String name, SimpleChat plugin) {
        super(name, plugin);
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(deserializeTranslatable(sender, "sahara.errors.client_only"));
            return;
        }
        if(!player.hasPermission("simplechat.emojis.toggle")) {
            player.sendMessage(deserializeTranslatable(sender, "sahara.errors.missing_permissions"));
            return;
        }
        boolean active = !player.hasPermission("simplechat.emojis");
        User user = plugin.lp.getPlayerAdapter(Player.class).getUser(player);
        user.data().add(Node.builder("simplechat.emojis").value(active).build());
        plugin.lp.getUserManager().saveUser(user);

        String state = I18n.translate(player, "simplechat.commands.emoji." + (active ? "enabled" : "disabled"));
        deserializeTranslatable(
                sender,
                "simplechat.commands.emoji.success",
                Placeholder.unparsed("state", state)
        );
    }

    @Override
    public List<String> complete(CommandSender sender, String prefix, String[] args) {
        return null;
    }
}
