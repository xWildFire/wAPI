package pl.wildfire.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by WildFire © 2015
 */
public class PlayerMap<V> implements Map<Player, V> {

    private final V defaultValue;
    private final Map<UUID, V> contents;

    public PlayerMap(){
        contents = new HashMap<>();
        defaultValue = null;
    }

    public void clear() {
        contents.clear();
    }

    public boolean containsKey(Object key) {
        if(key instanceof Player)
            return contents.containsKey(((Player) key).getUniqueId());
        if(key instanceof String)
            return contents.containsKey(key);
        return false;
    }

    public boolean containsValue(Object value){
        return contents.containsValue(value);
    }

    public Set<Entry<Player, V>> entrySet() {
        Set<Entry<Player, V>> toReturn = new HashSet<>();
        for(UUID uuid : contents.keySet())
            toReturn.add(new PlayerEntry(Bukkit.getPlayer(uuid), contents.get(uuid)));
        return toReturn;
    }

    public V get(Object key) {
        V result = null;
        if(key instanceof Player)
            result = contents.get(((Player) key).getUniqueId());
        if(key instanceof String)
            result = contents.get(key);
        return (result == null) ? defaultValue : result;
    }

    public boolean isEmpty(){
        return contents.isEmpty();
    }

    public Set<Player> keySet(){
        Set<Player> toReturn = new HashSet<Player>();
        for(UUID uuid : contents.keySet())
            toReturn.add(Bukkit.getPlayer(uuid));
        return toReturn;
    }

    public V put(Player key, V value) {
        if(key == null)
            return null;
        return contents.put(key.getUniqueId(), value);
    }

    public void putAll(Map<? extends Player, ? extends V> map) {
        for(Entry<? extends Player, ? extends V> entry : map.entrySet())
            put(entry.getKey(), entry.getValue());
    }

    public V remove(Object key) {
        if(key instanceof Player)
            return contents.remove(((Player) key).getUniqueId());
        if(key instanceof String)
            return contents.remove(key);
        return null;
    }

    public int size() {
        return contents.size();
    }

    public Collection<V> values() {
        return contents.values();
    }

    public String toString(){
        return contents.toString();
    }

    public class PlayerEntry implements Entry<Player, V>{

        private Player key;
        private V value;

        public PlayerEntry(Player key, V value){
            this.key = key;
            this.value = value;
        }

        public Player getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V toReturn = this.value;
            this.value = value;
            return toReturn;
        }

    }
}