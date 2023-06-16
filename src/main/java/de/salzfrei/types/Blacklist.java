package de.salzfrei.types;

import de.salzfrei.Main;
import de.salzfrei.objects.User;

public class Blacklist extends Type {

    public Blacklist() {}

    @Override
    public boolean isAllowed(User u, String c) {
        return u.hasPermission(Main.getBypassPermission()) || !this.match(c);
    }

}
