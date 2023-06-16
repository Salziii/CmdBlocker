package de.salzfrei.events;

import com.mojang.brigadier.tree.CommandNode;
import de.salzfrei.Main;
import de.salzfrei.objects.cache.CommandMemory;
import de.salzfrei.objects.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Chat implements Listener {

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onChat(ChatEvent e) {
        if (e.isCommand() && e.getSender() instanceof ProxiedPlayer) {
            User u = new User((ProxiedPlayer) e.getSender());

            if (Main.getType().isAllowed(u, "help")
                    && Pattern.compile("help").matcher(e.getMessage()).find()
            ) {
                StringBuilder sb = new StringBuilder();
                sb.append("§7§lHelp\n\n§r");
                for (CommandNode<?> commandNode : CommandMemory.MEMORY.get(u.getPlayer().getUniqueId())) {
                    String c = commandNode.getName();
                    if (Main.getType().isAllowed(u, "/" + c)) {
                        sb.append(ChatColor.DARK_GRAY).append("- ").append(ChatColor.GRAY).append("/").append(c).append("\n");
                    }
                }
                sb.delete(sb.length()-1, sb.length()); // Delete last '/n'
                ((ProxiedPlayer) e.getSender()).sendMessage(sb.toString());
                e.setCancelled(true);
                return;
            }

            ArrayList<String> validCommands = new ArrayList<>();
            CommandMemory.MEMORY.get(u.getPlayer().getUniqueId()).forEach(commandNode -> validCommands.add("/" + commandNode.getName()));

            String command = Arrays.stream(e.getMessage().split(" ")).toList().get(0);
            if (!Main.getType().isAllowed(u, command)
                    || !validCommands.contains(command)
            ) {
                e.setCancelled(true);
                u.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getDenyMessage()));
            }

        }
    }

}
