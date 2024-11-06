package com.rappytv.simplechat.listeners;

import com.rappytv.simplechat.SimpleChat;
import com.rappytv.simplechat.commands.ChatCommand;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.funoase.sahara.bukkit.util.Colors;
import net.funoase.sahara.bukkit.util.Permissions;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.*;

@SuppressWarnings("ConstantConditions")
public class PlayerChatListener implements Listener, ChatRenderer, PluginMessageListener {

    private static final MiniMessage minimessage = MiniMessage.miniMessage();
    private final SimpleChat plugin;

    public PlayerChatListener(SimpleChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String message = ((TextComponent) event.originalMessage()).content();

        if(!player.hasPermission("simplechat.chat.use")) {
            player.sendMessage(ChatCommand.deserializeTranslatable(player, "simplechat.listener.missing_permissions"));
            event.setCancelled(true);
            return;
        }
        if(!ChatCommand.isEnabled() && !player.hasPermission("simplechat.manage.chat.toggle.bypass")) {
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
            boolean isBungee = section.getBoolean("bungee");
            message = message.substring(words[0].length() + 1);
            Component component = minimessage.deserialize(
                    SimpleChat.setPlaceholders(
                            player,
                            plugin.getConfig().getString("chat.channel_format")
                    ),
                    Placeholder.parsed("name", section.getString("name")),
                    Placeholder.unparsed("player", player.getName()),
                    Placeholder.component("message", Colors.translateCodes(message))
            );
            broadcastChannelMessage(player, sectionName, JSONComponentSerializer.json().serialize(component), isBungee);
            return;
        }

        event.renderer(this);
        event.message(Colors.translatePlayerCodes(player, message, "simplechat.chat.format"));
    }

    @Override
    public @NotNull Component render(@NotNull Player player, @NotNull Component displayName, @NotNull Component message, @NotNull Audience audience) {
        boolean margin = player.hasPermission("simplechat.chat.format.margin");
        String marginText = plugin.getConfig().getString("chat.margin");
        String prefix = plugin.getLuckPermsUtil().getPrefix(player);
        String suffix = plugin.getLuckPermsUtil().getSuffix(player);

        return minimessage.deserialize(
                SimpleChat.setPlaceholders(
                        player,
                        plugin.getConfig().getString("chat.message_format")
                ),
                Placeholder.parsed(
                        "prefix_format",
                        SimpleChat.setPlaceholders(
                                player,
                                !prefix.isEmpty() ? plugin.getConfig().getString("chat.prefix_format") : ""
                        )
                ),
                Placeholder.parsed("player_prefix", prefix),
                Placeholder.parsed(
                        "suffix_format",
                        SimpleChat.setPlaceholders(
                                player,
                                !suffix.isEmpty() ? plugin.getConfig().getString("chat.suffix_format") : ""
                        )
                ),
                Placeholder.parsed("player_suffix", suffix),
                Placeholder.unparsed("player", player.getName()),
                Placeholder.parsed("margin1", margin ? marginText + "\n" : ""),
                Placeholder.parsed("margin2", margin ? "\n" + marginText : ""),
                Placeholder.component("message", message)
        );
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        if(!channel.equals("BungeeCord")) return;

        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
            String subchannel = in.readUTF();
            if(!subchannel.equals("SimpleChat")) return;

            short length = in.readShort();
            byte[] msgbytes = new byte[length];
            in.readFully(msgbytes);

            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            String targetChannel = msgin.readUTF();
            String playerMessage = msgin.readUTF();
            long sentAt = msgin.readLong();
            if(System.currentTimeMillis() > sentAt + 5000) return;
            broadcastChannelMessage(player, targetChannel, playerMessage, false);
        } catch (IOException ignored) {}
    }

    private void broadcastChannelMessage(Player sender, String channel, String component, boolean bungee) {
        if(bungee) {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(stream);

                out.writeUTF("Forward");
                out.writeUTF("ALL");
                out.writeUTF("SimpleChat");

                ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                DataOutputStream msgout = new DataOutputStream(msgbytes);
                msgout.writeUTF(channel);
                msgout.writeUTF(component);
                msgout.writeLong(System.currentTimeMillis());

                out.writeShort(msgbytes.toByteArray().length);
                out.write(msgbytes.toByteArray());

                sender.sendPluginMessage(plugin, "BungeeCord", stream.toByteArray());
            } catch (IOException ignored) {}
        }
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("channels." + channel);
        if(section == null) return;
        String permission = section.getString("permission");
        Component message = JSONComponentSerializer.json().deserialize(component);
        for(Player target : Bukkit.getOnlinePlayers()) {
            if(Permissions.hasExactPermission(target, "simplechat.channel.*") || Permissions.hasExactPermission(target, permission))
                target.sendMessage(message);
        }
        plugin.getServer().getConsoleSender().sendMessage(message);
    }
}
