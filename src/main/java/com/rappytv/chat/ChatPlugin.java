package com.rappytv.chat;

import com.rappytv.chat.commands.Chat;
import com.rappytv.chat.commands.ChatClear;
import com.rappytv.chat.commands.Emoji;
import com.rappytv.chat.events.JoinListener;
import com.rappytv.chat.events.PlayerChatListener;
import com.rappytv.chat.events.luckperms.UpdateListener;
import com.rappytv.chat.util.LuckPermsUtil;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ChatPlugin extends JavaPlugin {

    public final static String prefix = "§e§lChat §8» §7";
    public LuckPerms lp;
    private LuckPermsUtil luckPermsUtil;

    @Override
    public void onEnable() {
        registerAll();
        saveDefaultConfig();

        LuckPerms provider = Bukkit.getServicesManager().load(LuckPerms.class);
        if (provider != null) {
            lp = provider;
            getLogger().info("Luckperms successfully loaded!");
            new UpdateListener(this);
        }
        luckPermsUtil = new LuckPermsUtil(this);
        Bukkit.getServicesManager().register(LuckPermsUtil.class, luckPermsUtil, this, ServicePriority.Normal);
        for(Player all : Bukkit.getOnlinePlayers()) {
            luckPermsUtil.setTabPrefix(all);
        }
    }

    @Override
    public void onDisable() {}

    private void registerAll() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(this), this);
        pm.registerEvents(new PlayerChatListener(this), this);

        Objects.requireNonNull(Bukkit.getPluginCommand("chat")).setExecutor(new Chat());
        Objects.requireNonNull(Bukkit.getPluginCommand("chatclear")).setExecutor(new ChatClear());
        Objects.requireNonNull(Bukkit.getPluginCommand("emoji")).setExecutor(new Emoji());
    }

    public LuckPermsUtil getLuckPermsUtil() {
        return luckPermsUtil;
    }
}
