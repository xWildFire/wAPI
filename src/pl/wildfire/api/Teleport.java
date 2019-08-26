package pl.wildfire.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Created by WildFire © 2015
 */
public class Teleport extends BukkitRunnable {
    private UUID uuid;
    private Location from;
    private Location to;
    private int now = 0;
    private int time;

    public Teleport(Player p, int time, Location to){
        this.uuid = p.getUniqueId();
        this.from = p.getLocation();
        this.to = to;
        this.time = time;

        Msg.send(getPlayer(), "&7Za &6"+time+"sec &7zostaniesz przeteleportowany!");

        super.runTaskTimer(Bukkit.getPluginManager().getPlugins()[0], 20, 20);
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public void run() {
        Player p = getPlayer();

        if(p == null) {
            cancel();
        }else if(p.getLocation().getX() != from.getX() || p.getLocation().getZ() != from.getZ() || p.getLocation().getY() != from.getY()){
            cancel();
            Msg.send(getPlayer(), "&4Blad: &cWykryto ruch! Teleportacja przerwana.");
        }else if(now >= time) {
            if(p.teleport(to))
                Msg.send(getPlayer(), "&7Teleportowano.");
            cancel();
        }else{
            now++;
        }
    }
}
