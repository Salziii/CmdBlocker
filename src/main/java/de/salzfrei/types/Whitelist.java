package de.salzfrei.types;

import de.salzfrei.Main;
import de.salzfrei.objects.Type;
import de.salzfrei.objects.User;

public class Whitelist extends Type {

    public Whitelist() {}

    @Override
    public boolean isAllowed(User u, String c) {
        return u.hasPermission(Main.getBypassPermission()) || this.match(c);
    }
}
