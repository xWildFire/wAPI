package pl.wildfire.api.cfg;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.wildfire.api.Msg;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Config extends YamlConfiguration {
    private final byte[] BUF = new byte[1024];
    private final File file;

    public Config(File file) {
        this(file, null);
    }

    public Config(File file, InputStream in) {
        this.file = file;
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();

                if (in == null) {
                    file.createNewFile();
                }else{
                    try (OutputStream out = new FileOutputStream(file)) {
                        int len;
                        while ((len = in.read(BUF)) > 0) {
                            out.write(BUF, 0, len);
                        }
                    }
                    in.close();
                }
            }
            super.load(file);
        } catch (Exception e) {
            Msg.exception(e);
        }
    }

    public void load() {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            super.load(file);
        } catch (Exception e) {
            Msg.exception(e);
        }
    }

    public void save() {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            super.save(file);
        } catch (Exception e) {
            Msg.exception(e);
        }
    }

}
