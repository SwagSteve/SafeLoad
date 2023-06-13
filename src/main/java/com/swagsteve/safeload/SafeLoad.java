package com.swagsteve.safeload;

import Commands.ReloadCommand;
import Listeners.Events;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public final class SafeLoad extends JavaPlugin {

    //Pack loaded
    public static ArrayList<Player> packLoaded;

    //Instance
    private static SafeLoad instance;
    public static SafeLoad getInstance(){
        return instance;
    }

    //Config cache values
    public static Boolean disable_chat, suppress_join_message, suppress_quit_message, invisibility, blindness, kick_if_rejected;
    public static String delayed_join_message, kick_message;
    public static Integer event_cancel_delay;

    @Override
    public void onEnable() {

        //Instance
        instance = this;

        //Config
        File tempConfig = new File(getDataFolder(), "config.yml");
        if (!tempConfig.exists()) {
            getLogger().info("Generating config...");
            saveDefaultConfig();
            reloadConfig();
        }

        cacheConfig();

        //Command
        this.getCommand("sl-reload").setExecutor(new ReloadCommand());

        //Events
        packLoaded = new ArrayList<>();
        getServer().getPluginManager().registerEvents(new Events(), this);

        //Enabled message
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        //Disabled message
        getLogger().info("Disabled!");

        //Save config
        saveConfig();
    }

    public static void cacheConfig() {

        //Config
        FileConfiguration config = SafeLoad.getInstance().getConfig();

        //Booleans
        SafeLoad.suppress_join_message = config.getBoolean("Options.suppress-join-message");
        SafeLoad.suppress_quit_message = config.getBoolean("Options.suppress-quit-message");
        SafeLoad.invisibility = config.getBoolean("Effects.invisibility");
        SafeLoad.blindness = config.getBoolean("Effects.blindness");
        SafeLoad.kick_if_rejected = config.getBoolean("Options.kick-if-rejected");
        SafeLoad.disable_chat = config.getBoolean("Options.disable-chat");

        //Strings
        SafeLoad.delayed_join_message = config.getString("Options.delayed-join-message");
        SafeLoad.kick_message = config.getString("Options.kick-message");

        //Integers
        SafeLoad.event_cancel_delay = config.getInt("Options.event-cancel-delay");
    }
}
