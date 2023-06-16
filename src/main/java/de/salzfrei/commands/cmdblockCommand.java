package de.salzfrei.commands;

import de.salzfrei.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class cmdblockCommand extends Command implements TabExecutor {

    public cmdblockCommand() {
        super("cmdblocker", "cmdblocker.command", "");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("cmdblocker.command")) {
            sender.sendMessage(Main.getDenyMessage());
            return;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                sender.sendMessage(ChatColor.RED + "Reloading...");
                Main.getInstance().reload();
                sender.sendMessage(ChatColor.GREEN + "Successfully reloaded!");
            } else if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage(ChatColor.BLUE + "Command Blocker v1.0.0\nBy Salzfrei\nCreated for MLMServer");
            }
        }  else {
            sender.sendMessage("Usage: /cmdblocker [reload (rl) | info]");
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] a) {
        if (a.length > 1) {
            return List.of("");
        } else {
            return Stream.of("reload", "rl", "info").filter(s -> a.length == 0 || s.toLowerCase().contains(a[0].toLowerCase())).collect(Collectors.toList());
        }
    }

}
