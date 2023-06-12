package Utils;

import org.bukkit.ChatColor;

public class Utils {
    public static String Color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}