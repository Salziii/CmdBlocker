package de.salzfrei;

import de.salzfrei.commands.cmdblockCommand;
import de.salzfrei.config.Config;
import de.salzfrei.config.ConfigLoader;
import de.salzfrei.events.*;
import de.salzfrei.objects.CommandMemory;
import de.salzfrei.objects.TabMemory;
import de.salzfrei.types.Blacklist;
import de.salzfrei.objects.Type;
import de.salzfrei.types.Whitelist;
import dev.simplix.protocolize.api.Protocolize;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class Main extends Plugin {

    public static Main instance;
    private static Config config;
    private static String denyMessage;
    private static Type type;
    private static String bypassPermission;
    private static List<String> patterns;
    private PacketDeclareCommandsListener packetDeclareCommandsListener;
    private PacketTabRequestListener packetTabRequestListener;
    private PacketTabResponseListener packetTabResponseListener;

    @Override
    public void onEnable() {
        instance = this;

        config = new ConfigLoader(new File("plugins/CMDBlocker"), "config.yml").reloadConfig();

        denyMessage = config.getString("deny message");
        type = (config.getString("type").equalsIgnoreCase("whitelist")) ? new Whitelist() : (config.getString("type").equalsIgnoreCase("blacklist")) ? new Blacklist() : null;
        bypassPermission = config.getString("bypass permission");
        patterns = config.getStringList("patterns");

        if (getProxy().getPluginManager().getPlugin("Protocolize") != null) {
            registerEvent(new PlayerListener());
            this.packetDeclareCommandsListener = new PacketDeclareCommandsListener();
            this.packetTabRequestListener = new PacketTabRequestListener();
            this.packetTabResponseListener = new PacketTabResponseListener();
            Protocolize.listenerProvider().registerListener(packetDeclareCommandsListener);
            Protocolize.listenerProvider().registerListener(packetTabRequestListener);
            Protocolize.listenerProvider().registerListener(packetTabResponseListener);
        } else
            getLogger().warning("Protocolize is dependent! Protocolize is required to prevent listing and tab-completion of commands!");

        registerEvent(new CommandEvent());
        registerCommands();

        getLogger().info(ChatColor.YELLOW + "\n-------------------------\nCommandBlocker\nv1.0.0 | by Salzfrei\nCreated for MLMServer\n-------------------------");

    }

    @Override
    public void onDisable() {
        try {
            if (packetDeclareCommandsListener != null) Protocolize.listenerProvider().unregisterListener(packetDeclareCommandsListener);
            if (packetTabRequestListener != null) Protocolize.listenerProvider().unregisterListener(packetTabRequestListener);
            if (packetTabResponseListener != null) Protocolize.listenerProvider().unregisterListener(packetTabResponseListener);
        } catch (IllegalArgumentException ignored) {}
    }

    public void reload() {
        config = new ConfigLoader(new File("plugins/CMDBlocker"), "config.yml").reloadConfig();

        denyMessage = config.getString("deny message");
        type = (config.getString("type").equalsIgnoreCase("whitelist")) ? new Whitelist() : (config.getString("type").equalsIgnoreCase("blacklist")) ? new Blacklist() : null;
        bypassPermission = config.getString("bypass permission");
        patterns = config.getStringList("patterns");
    }

    public static String getDenyMessage() {
        return denyMessage;
    }
    public static Type getType() {
        return type;
    }
    public static String getBypassPermission() {
        return bypassPermission;
    }
    public static List<String> getPatterns() {
        return patterns;
    }
    public Collection<Map.Entry<String, Command>> getCommands() {
        getProxy().getPluginManager().getCommands();
        getProxy().getPluginManager().getCommands();

        return null;
    }
    public static Main getInstance() {
        return instance;
    }

    public <T extends Listener> void registerEvent(T l) {
        ProxyServer.getInstance().getPluginManager().registerListener(this, l);
    }
    public void registerCommands() {
        getProxy().getPluginManager().registerCommand(this, new cmdblockCommand());
    }

}
