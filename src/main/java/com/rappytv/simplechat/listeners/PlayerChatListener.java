package com.rappytv.simplechat.listeners;

import com.rappytv.simplechat.SimpleChat;
import com.rappytv.simplechat.commands.ChatCommand;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.funoase.sahara.bukkit.util.Permissions;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@SuppressWarnings("ConstantConditions")
public class PlayerChatListener implements Listener {

    private static final MiniMessage minimessage = MiniMessage.miniMessage();
    private final SimpleChat plugin;

    public PlayerChatListener(SimpleChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String message = ((TextComponent) event.originalMessage()).content();

        if(!ChatCommand.isEnabled() && !player.hasPermission("simplechat.chat.manage.toggle.bypass")) {
            player.sendMessage(ChatCommand.deserializeTranslatable(player, "simplechat.listener.chat_disabled"));
            event.setCancelled(true);
            return;
        }
        if(player.hasPermission("simplechat.emojis")) {
            if(plugin.getConfig().contains("emojis")) {
                for(String emoji : plugin.getConfig().getStringList("emojis")) {
                    try {
                        String[] emojis = emoji.split(";");
                        message = message.replaceAll(emojis[0], emojis[1]);
                    } catch (IndexOutOfBoundsException ignored) {
                    }
                }
            }
        }
        // Translate color codes
//        event.setMessage(Colors.translatePlayerCodes(player, event.getMessage(), "chat.format"));

        String[] words = message.split(" ");
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("channels");
        for(String sectionName : section.getKeys(false)) {
            section = section.getConfigurationSection(sectionName);
            String permission = section.getString("permission");
            boolean hasPermission = Permissions.hasExactPermission(
                    player,
                    "simplechat.channels.*"
            ) || Permissions.hasExactPermission(
                    player,
                    permission
            );
            if(!hasPermission || !section.getStringList("triggers").contains(words[0])) continue;
            event.setCancelled(true);
            if(words.length < 2) {
                player.sendMessage(ChatCommand.deserializeTranslatable(player, "simplechat.listener.enter_text"));
                return;
            }
            message = message.substring(words[0].length() + 1);
            for(Player target : Bukkit.getOnlinePlayers()) {
                if(Permissions.hasExactPermission(target, "simplechat.chat.*") || Permissions.hasExactPermission(target, permission))
                    target.sendMessage(minimessage.deserialize(
                            SimpleChat.setPlaceholders(
                                    player,
                                    plugin.getConfig().getString("chat.channel_format")
                            ),
                            Placeholder.unparsed("name", section.getString("name")),
                            Placeholder.unparsed("player", player.getName()),
                            Placeholder.unparsed("message", message)
                    ));
            }
            return;
        }

        boolean margin = player.hasPermission("simplechat.chat.format.margin");
        String marginText = plugin.getConfig().getString("chat.margin");
        String prefix = plugin.getLuckPermsUtil().getPrefix(player);
        String suffix = plugin.getLuckPermsUtil().getSuffix(player);
        event.message(minimessage.deserialize(
                SimpleChat.setPlaceholders(
                        player,
                        plugin.getConfig().getString("chat.message_format")
                ),
                Placeholder.unparsed(
                        "prefix_format",
                        SimpleChat.setPlaceholders(
                                player,
                                !prefix.isEmpty() ? plugin.getConfig().getString("chat.prefix_format") : ""
                        )
                ),
                Placeholder.parsed("player_prefix", prefix),
                Placeholder.unparsed(
                        "suffix_format",
                        SimpleChat.setPlaceholders(
                                player,
                                !suffix.isEmpty() ? plugin.getConfig().getString("chat.suffix_format") : ""
                        )
                ),
                Placeholder.parsed("player_suffix", suffix),
                Placeholder.unparsed("player", player.getName()),
                Placeholder.unparsed("margin1", margin ? marginText + "\n" : ""),
                Placeholder.unparsed("margin2", margin ? "\n" + marginText : ""),
                Placeholder.unparsed("message", message)
        ));
    }
}