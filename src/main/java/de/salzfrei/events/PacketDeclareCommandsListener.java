package de.salzfrei.events;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import de.salzfrei.objects.CommandMemory;
import de.salzfrei.objects.ProtocolizePacketListener;
import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import de.salzfrei.Main;
import de.salzfrei.objects.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.packet.Commands;

import java.util.Iterator;

public class PacketDeclareCommandsListener extends ProtocolizePacketListener<Commands> {

    public PacketDeclareCommandsListener() {
        super(Commands.class, Direction.UPSTREAM, 0);
    }

    @Override
    public void packetSend(PacketSendEvent<Commands> e) {
        CommandMemory.MEMORY.put(e.player().uniqueId(), e.packet().getRoot().getChildren());
        RootCommandNode<?> root = e.packet().getRoot();
        Object sender = e.player().handle();
        if (root != null && sender instanceof ProxiedPlayer) {
            filterNodes(new User((ProxiedPlayer) sender), root);
        }
    }

    @Override
    public void packetReceive(PacketReceiveEvent<Commands> e) {}

    private void filterNodes(User u, CommandNode<?> node) {
        Iterator<? extends CommandNode<?>> it = node.getChildren().iterator();
        while (it.hasNext()) {
            node = it.next();
            if (!Main.getType().isAllowed(u, "/" + node.getName())) {
                it.remove();
            }
        }
    }

}

