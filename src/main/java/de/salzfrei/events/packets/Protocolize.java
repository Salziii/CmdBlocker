package de.salzfrei.events.packets;

import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;

public abstract class Protocolize<T> extends AbstractPacketListener<T> {

    protected Protocolize(Class<T> type, Direction direction, int priority) {
        super(type, direction, priority);
    }

}
