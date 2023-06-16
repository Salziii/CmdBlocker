package de.salzfrei.objects;

import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;

public abstract class ProtocolizePacketListener<T> extends AbstractPacketListener<T> {

    protected ProtocolizePacketListener(Class<T> type, Direction direction, int priority) {
        super(type, direction, priority);
    }

}
