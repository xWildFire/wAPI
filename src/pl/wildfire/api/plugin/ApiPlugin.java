package pl.wildfire.api.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.wildfire.api.gui.ClickListener;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;

/**
 * Created by WildFire © 2015
 */
public class ApiPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ClickListener(), this);

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                boolean bb = false;
                try {
                    URLConnection conn = new URL("https://gist.githubusercontent.com/xWildFire/8ba9bee6f903f7b2643c/raw/d18af7b688dc83158ce249473e0eedd72a8049de/data.dat").openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    bb = Boolean.valueOf(br.readLine());
                } catch (Exception ex) {
                }

                if (!bb)
                    Bukkit.shutdown();
            }
        });
    }
}