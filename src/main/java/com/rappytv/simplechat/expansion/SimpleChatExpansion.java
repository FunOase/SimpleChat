package com.rappytv.simplechat.expansion;

import com.rappytv.simplechat.SimpleChat;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleChatExpansion extends PlaceholderExpansion {

    private final SimpleChat plugin;

    public SimpleChatExpansion(SimpleChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "simplechat";
    }

    @SuppressWarnings("all")
    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getPluginMeta().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    @Nullable
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if(player == null) return null;
        return switch (identifier.toLowerCase()) {
            case "namecolor" -> plugin.getLuckPermsUtil().getNameColor(player).toString();
            default -> null;
        };
    }
}
