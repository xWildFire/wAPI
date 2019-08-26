package pl.wildfire.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.*;

public class Bar {

    private final UUID player;
    private Object packet;
    private float health;
    private float remove;

    public Bar(Player player, Object packet) {
        this.player = player.getUniqueId();
        this.packet = packet;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(player);
    }

    public Object getPacket() {
        return packet;
    }

    public void setPacket(Object packet) {
        this.packet = packet;
    }

    public float getRemove() {
        return remove;
    }

    public void setRemove(float f) {
        remove = f;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float hp) {
        try {
            this.packet.getClass().getMethod("setHealth", new Class[]{Float.TYPE}).invoke(this.packet, new Object[]{hp + 0.1F});
            Object live = Reflections.getMinecraftClass("PacketPlayOutSpawnEntityLiving").getConstructor(new Class[]{Reflections.getMinecraftClass("EntityLiving")}).newInstance(new Object[]{this.packet});
            send(getPlayer(), live);
            this.health = hp;
        } catch (Exception e) {
            Msg.exception(e);
        }
    }

    public String getMessage() {
        try {
            return (String) this.packet.getClass().getMethod("getCustomName", new Class[0]).invoke(this.packet, new Object[0]);
        } catch (Exception e) {
        }
        return "";
    }

    public void setMessage(String text) {
        try {
            this.packet.getClass().getMethod("setCustomName", new Class[]{String.class}).invoke(this.packet, new Object[]{text});
            Object message = Reflections.getMinecraftClass("PacketPlayOutSpawnEntityLiving").getConstructor(new Class[]{Reflections.getMinecraftClass("EntityLiving")}).newInstance(new Object[]{this.packet});
            send(getPlayer(), message);
        } catch (Exception e) {
            Msg.exception(e);
        }
    }

    public void remove() {
        try {
            int id = (Integer) packet.getClass().getMethod("getId").invoke(packet, new Object[0]);
            Object ender = Reflections.getMinecraftClass("PacketPlayOutEntityDestroy").getConstructor(int[].class).newInstance(new int[]{id});
            send(getPlayer(), ender);
            PLAYERS.remove(player);
        } catch (Exception e) {
            Msg.exception(e);
        }
    }

    private static final Map<UUID, Bar> PLAYERS = new HashMap<>();

    public static Bar getBar(Player p) {
        if (PLAYERS.containsKey(p.getUniqueId()))
            return PLAYERS.get(p.getUniqueId());
        return null;
    }

    public static void removeBars() {
        for (Bar ba : new ArrayList<>(list())) {
            ba.remove();
        }
        PLAYERS.clear();
    }

    public static Collection<Bar> list() {
        return PLAYERS.values();
    }

    public static Bar spawnBar(Player p, int czas, float hp, String text) {
        if (getBar(p) != null) {
            try {
                Object ender = Reflections.getMinecraftClass("EntityEnderDragon").getConstructor(Reflections.getMinecraftClass("World")).newInstance(Reflections.getCraftBukkitClass("CraftWorld").getMethod("getHandle").invoke(p.getWorld()));
                ender.getClass().getMethod("setLocation", double.class, double.class, double.class, float.class, float.class).invoke(ender, p.getLocation().getX(), -500, p.getLocation().getZ(), 0F, 0F);
                ender.getClass().getMethod("setCustomName", String.class).invoke(ender, Msg.c(text));
                ender.getClass().getMethod("setCustomNameVisible", boolean.class).invoke(ender, false);
                ender.getClass().getMethod("setHealth", float.class).invoke(ender, hp + 0.1F);
                send(p, Reflections.getMinecraftClass("PacketPlayOutSpawnEntityLiving").getConstructor(Reflections.getMinecraftClass("EntityLiving")).newInstance(ender));
                final Bar ba = new Bar(p, ender);
                PLAYERS.put(p.getUniqueId(), ba);

                if (czas != -1) {
                    ba.setHealth(200.0F);
                    ba.setRemove(200.0F / czas);
                }
                return ba;
            } catch (Exception e) {
                Msg.exception(e);
            }
        } else {
            return changeBar(p, hp, Msg.c(text), czas);
        }
        return null;
    }

    public static Bar spawnBar(Player p, int czas, String text) {
        if (getBar(p) != null) {
            return spawnBar(p, czas, 200, text);
        } else {
            return changeBar(p, 200, text, czas);
        }
    }

    public static Bar spawnBar(Player p, String text) {
        if (getBar(p) != null) {
            return spawnBar(p, 200, text);
        } else {
            return changeBar(p, 200, text, -1);
        }
    }

    public static Bar spawnBar(Player p, float hp, String text) {
        if (getBar(p) != null) {
            return spawnBar(p, -1, hp, text);
        } else {
            return changeBar(p, hp, text, -1);
        }
    }

    private static Bar changeBar(Player p, float hp, String text, long czas) {
        Bar ba = getBar(p);
        if (ba != null) {
            try {
                Object ender = ba.getPacket();
                int id = (Integer) ender.getClass().getMethod("getId").invoke(ender, new Object[0]);
                Object watcher = Reflections.getMinecraftClass("DataWatcher").getConstructor(Reflections.getMinecraftClass("Entity")).newInstance(ender);
                Method a = watcher.getClass().getMethod("a", int.class, Object.class);
                a.invoke(watcher, 0, (byte) 0x20);
                a.invoke(watcher, 6, (float) hp + 0.1F);
                a.invoke(watcher, 8, (byte) 0);
                a.invoke(watcher, 10, Msg.c(text));
                a.invoke(watcher, 11, (byte) 0);
                ender.getClass().getMethod("setCustomName", String.class).invoke(ender, Msg.c(text));
                Object packet = Reflections.getMinecraftClass("PacketPlayOutEntityMetadata").getConstructor(int.class, Reflections.getMinecraftClass("DataWatcher"), boolean.class).newInstance(id, watcher, true);
                send(p, packet);
                ba.setPacket(ender);

                if (czas != -1) {
                    ba.setHealth(200.0F);
                    ba.setRemove(200.0F / czas);
                }
                return ba;
            } catch (Exception e) {
                Msg.exception(e);
            }
        }
        return null;
    }

    private static void send(Player p, Object packet) {
        try {
            Object cp = Reflections.getCraftBukkitClass("entity.CraftPlayer").getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
            Object con = Reflections.getMinecraftClass("EntityPlayer").getField("playerConnection").get(cp);
            Method send = Reflections.getMinecraftClass("PlayerConnection").getMethod("sendPacket", new Class[]{Reflections.getMinecraftClass("Packet")});
            send.invoke(con, packet);
        } catch (Exception e) {
            Msg.exception(e);
        }
    }
}
