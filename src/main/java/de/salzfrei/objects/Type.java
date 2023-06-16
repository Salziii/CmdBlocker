package de.salzfrei.objects;

import de.salzfrei.Main;
import de.salzfrei.objects.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public abstract class Type {



    public abstract boolean isAllowed(User u, String c);

    public boolean match(String command) {
        ArrayList<Pattern> rawPatterns = new ArrayList<>();
        String[] regexes = Main.getPatterns().toArray(new String[Main.getPatterns().size() - 1]);
        String[] rawPatternRegexes;
        synchronized (this) {
            rawPatternRegexes = Arrays.copyOf(regexes, regexes.length);
        }
        for (String rawPatternRegex : rawPatternRegexes) {
            rawPatterns.add(Pattern.compile(rawPatternRegex));
        }
        for (Pattern pattern : rawPatterns) {
            if (pattern.matcher(command).find()) return true;
        }
        return false;
    }

    public boolean checkPermission(User u) {
        return Main.getBypassPermission() != null && u.hasPermission(Main.getBypassPermission());
    }

}
