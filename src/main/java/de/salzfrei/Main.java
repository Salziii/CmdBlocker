package de.salzfrei;

import de.salzfrei.commands.cmdblocker;
import de.salzfrei.config.Config;
import de.salzfrei.config.ConfigLoader;
import de.salzfrei.events.*;
import de.salzfrei.events.packets.DeclareCommand;
import de.salzfrei.events.packets.TabRequest;
import de.salzfrei.events.packets.TabResponse;
import de.salzfrei.types.Blacklist;
import de.salzfrei.types.Type;
import de.salzfrei.types.Whitelist;
import dev.simplix.protocolize.api.Protocolize;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.List;

public final class Main extends Plugin {

    // Instance
    public static Main instance;

    // Config
    private static String denyMessage;
    private static Type type;
    private static String bypassPermission;
    private static List<String> patterns;

    // Protocolize Listeners
    private DeclareCommand declareCommandPacketListener;
    private TabRequest tabRequestPacketListener;
    private TabResponse tabResponsePacketListener;

    @Override
    public void onEnable() {
        instance = this;
        reloadConfig();
        registerProtocolizeEvents();
        registerEvent(new Chat());
        registerCommand(new cmdblocker());
        getLogger().info(ChatColor.YELLOW + "\n-------------------------\nCommandBlocker\nv1.0.0 | by Salzfrei\nCreated for MLMServer\n-------------------------");
    }

    @Override
    public void onDisable() {
        deregisterProtocolizeEvents();
    }

    // GETTERS
    public static Main getInstance() {
        return instance;
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

    // FUNCTIONS
    public void reloadConfig() {
        Config config = new ConfigLoader(new File("plugins/CMDBlocker"), "config.yml").reloadConfig();

        denyMessage = config.getString("deny message");
        type = (config.getString("type").equalsIgnoreCase("whitelist")) ? new Whitelist() : (config.getString("type").equalsIgnoreCase("blacklist")) ? new Blacklist() : null;
        bypassPermission = config.getString("bypass permission");
        patterns = config.getStringList("patterns");
    }
    public void registerProtocolizeEvents() {
        if (getProxy().getPluginManager().getPlugin("Protocolize") != null) {
            registerEvent(new PlayerDisconnect());
            this.declareCommandPacketListener = new DeclareCommand();
            this.tabRequestPacketListener = new TabRequest();
            this.tabResponsePacketListener = new TabResponse();
            Protocolize.listenerProvider().registerListener(declareCommandPacketListener);
            Protocolize.listenerProvider().registerListener(tabRequestPacketListener);
            Protocolize.listenerProvider().registerListener(tabResponsePacketListener);
        } else
            getLogger().warning("Protocolize is dependent! Protocolize is required to prevent listing and tab-completion of commands!");
    }
    public void deregisterProtocolizeEvents() {
        try {
            if (declareCommandPacketListener != null) Protocolize.listenerProvider().unregisterListener(declareCommandPacketListener);
            if (tabRequestPacketListener != null) Protocolize.listenerProvider().unregisterListener(tabRequestPacketListener);
            if (tabResponsePacketListener != null) Protocolize.listenerProvider().unregisterListener(tabResponsePacketListener);
        } catch (IllegalArgumentException ignored) {}
    }
    private <T extends Listener> void registerEvent(T l) {
        ProxyServer.getInstance().getPluginManager().registerListener(this, l);
    }
    private <T extends Command> void registerCommand(T c) {
        getProxy().getPluginManager().registerCommand(this, c);
    }

}
