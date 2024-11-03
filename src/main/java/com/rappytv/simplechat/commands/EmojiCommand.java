package com.rappytv.simplechat.commands;

import com.rappytv.simplechat.SimpleChat;
import net.funoase.sahara.bukkit.util.Command;
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
            sender.sendMessage(RyLib.get().i18n().translate("onlyPlayer"));
            return;
        }

        if(!player.hasPermission("simplechat.emojis.toggle")) {
            player.sendMessage(RyLib.get().i18n().translate("noPermission"));
            return;
        }
        boolean active = !player.hasPermission("simplechat.emojis");
        User user = plugin.lp.getPlayerAdapter(Player.class).getUser(player);
        user.data().add(Node.builder("chat.emojis").value(active).build());
        plugin.lp.getUserManager().saveUser(user);

        String text = active
                ? plugin.i18n().translate("command.emoji.activated")
                : plugin.i18n().translate("command.emoji.deactivated");
        player.sendMessage(
                plugin.i18n().translate(
                        "command.emoji.success",
                        new I18n.Argument("state", text)
                )
        );
    }

    @Override
    public List<String> complete(CommandSender sender, String prefix, String[] args) {
        return null;
    }
}
