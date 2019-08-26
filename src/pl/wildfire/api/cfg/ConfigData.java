package pl.wildfire.api.cfg;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.wildfire.api.Msg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by WildFire © 2015
 */
public class ConfigData extends YamlConfiguration {
    private final byte[] BUF = new byte[1024];
    private final File file;

    public ConfigData(File file) {
        this(file, null);
    }

    public ConfigData(File file, InputStream in) {
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
            load();
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
            for (Field field : getClass().getFields()) {
                if(field.getDeclaringClass().equals(FileConfiguration.class) || field.getName().equalsIgnoreCase("instance"))
                    continue;
                if ((Modifier.isStatic(field.getModifiers())) && (Modifier.isPublic(field.getModifiers()))) {
                    String path = field.getName().toLowerCase().replace("$", "-").replace("_", ".");
                    if (isSet(path))
                        field.set(null, get(path) instanceof String ? Msg.c((String)get(path)) : get(path));
                }
            }
        } catch (Exception e) {
            Msg.exception(e);
        }
    }

    public void save() {
        try {
            for (Field field : getClass().getFields()) {
                if(field.getDeclaringClass().equals(FileConfiguration.class) || field.getName().equalsIgnoreCase("instance"))
                    continue;
                if ((Modifier.isStatic(field.getModifiers())) && (Modifier.isPublic(field.getModifiers()))) {
                    String path = field.getName().toLowerCase().replace("$", "-").replace("_", ".");
                    if (get(path) == null)
                        set(path, field.get(null));
                }
            }
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
