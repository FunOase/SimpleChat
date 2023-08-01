package com.rappytv.chat;

import com.rappytv.chat.commands.ChatCommand;
import com.rappytv.chat.commands.Emoji;
import com.rappytv.chat.events.JoinListener;
import com.rappytv.chat.events.PlayerChatListener;
import com.rappytv.chat.events.luckperms.UpdateListener;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Chat extends JavaPlugin {

    public final static String prefix = "§e§lChat §8» §7";
    public static LuckPerms lp;
    public static int maxWeight;

    @Override
    public void onEnable() {
        registerAll();
        saveDefaultConfig();
        maxWeight = getConfig().getInt("maxWeight");

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            lp = provider.getProvider();
            getServer().getLogger().info("Luckperms successfully loaded!");
            new UpdateListener(lp);
        }
    }

    @Override
    public void onDisable() {}

    public void registerAll() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new PlayerChatListener(this), this);

        Objects.requireNonNull(Bukkit.getPluginCommand("chat")).setExecutor(new ChatCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("emoji")).setExecutor(new Emoji());
    }
}
