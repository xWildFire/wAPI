package pl.wildfire.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Msg {

    private static final ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static boolean send(CommandSender c, String m) {
        if(c == null)
            return sendConsole(m);
        c.sendMessage(c(m));
        return true;
    }

    public static boolean send(Player c, String m) {
        if(c == null)
            return sendConsole(m);
        c.sendMessage(c(m));
        return true;
    }

    public static boolean sendConsole(String m) {
        console.sendMessage(c(m));
        return true;
    }

    public static boolean broadcast(String wiadomosc) {
        Bukkit.broadcastMessage(c(wiadomosc));
        return true;
    }

    public static boolean sendAll(String wiadomosc) {
        wiadomosc = c(wiadomosc);
        for(Player p : Bukkit.getOnlinePlayers())
            p.sendMessage(wiadomosc);
        return true;
    }

    public static boolean sendPerm(String m, String p){
        Bukkit.broadcast(c(m), p);
        return true;
    }

    public static boolean exception(Throwable e) {
        if (e == null) {
            return false;
        }
        return exception(e.getMessage(), e.getStackTrace());
    }

    public static boolean exception(String cause, StackTraceElement[] ste) {
        error("#!# #!# #!# #!# #!# #!# #!# #!#");
        if(cause != null)
            error("Caused by: " + cause);
        if (ste != null && ste.length > 0) {
            for (StackTraceElement st : ste) {
                error("  at " + st.toString());
            }
        } else {
            error("Stack trace: no/empty exception given, dumping current stack trace instead!");
        }
        error("#!# #!# #!# #!# #!# #!# #!# #!#");
        return true;
    }

    public static void error(String content) {
        Bukkit.getLogger().severe("#!# " + content);
    }

    public static String c(String w) {
        return ChatColor.translateAlternateColorCodes('&', w);
    }

    public static String s(String w) {
        return ChatColor.stripColor(w);
    }

    public static List<String> c(List<String> w) {
        List<String> l = new ArrayList<>();
        for(String s : w)
            l.add(ChatColor.translateAlternateColorCodes('&', s));
        return l;
    }
}
