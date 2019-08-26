package pl.wildfire.api;

import org.bukkit.ChatColor;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LogUtil {

    private static final SimpleDateFormat sdf =  new SimpleDateFormat("dd.MM.yy HH:mm:ss");
    private static final Executor executor = Executors.newSingleThreadExecutor();

    public static void log(final File f, final String s) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    try (BufferedWriter w = new BufferedWriter(new FileWriter(f, true)); BufferedReader r = new BufferedReader(new FileReader(f))) {
                        w.write(ChatColor.stripColor(s.replace("{DATE}", sdf.format(new Date()))));
                        w.newLine();
                        w.flush();
                    }
                } catch (IOException e) {

                }
            }
        });
    }

    public static void clear(File f) {
        try (PrintWriter writer = new PrintWriter(f)) {
            writer.print("");
        } catch (FileNotFoundException ex) {

        }
    }

}
