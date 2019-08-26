package pl.wildfire.api.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.wildfire.api.Msg;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Cmd extends Command {
    private boolean player = false;

    public Cmd(String name, String... aliases) {
        this(name, null, aliases);
    }

    public Cmd(String name, String permission, String... aliases) {
        super(name);
        if(aliases.length > 0)
            super.setAliases(Arrays.asList(aliases));
        super.setDescription("Command "+name);
        super.setUsage("/" + name);
        super.setPermission(permission != null && !permission.isEmpty() ? permission : null);
        super.setPermissionMessage("&4Blad: &cNie posiadasz uprawnien! &7(" + permission + ")");

        boolean good = false;
        try {
            Method m = getClass().getMethod("runCommand", CommandSender.class, String[].class);
            if (!m.equals(CMD_SENDER)) {
                good = true;
            }
        } catch (Exception e) {

        }

        try {
            Method m = getClass().getMethod("runCommand", Player.class, String[].class);
            if (!m.equals(CMD_PLAYER)) {
                good = true;
                player = true;
            }
        } catch (Exception e) {

        }


        if (good) {
            CMAP.register(getName(), this);
        }
    }

    public boolean runCommand(CommandSender sender, String[] args) {
        return Msg.send(sender, "&4Blad: &cNie mozliwe do wykonania z poziomu konsoli.");
    }

    public boolean runCommand(Player player, String[] args) {
        return Msg.send(player, "&4Blad: &cNie mozliwe do wykonania z poziomu gracza!");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (getPermission() != null && !sender.hasPermission(getPermission()))
            return Msg.send(sender, getPermissionMessage());

        if (player && sender instanceof Player)
            return runCommand((Player) sender, args);
        return runCommand((CommandSender)sender, args);
    }

    protected static CommandMap CMAP;
    protected static Method CMD_SENDER;
    protected static Method CMD_PLAYER;

    static {
        try {
            CMD_SENDER = Cmd.class.getMethod("runCommand", CommandSender.class, String[].class);
            CMD_PLAYER = Cmd.class.getMethod("runCommand", Player.class, String[].class);

            final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);

            CMAP = (CommandMap) f.get(Bukkit.getServer());
        }catch(Exception e){

        }
    }

}
