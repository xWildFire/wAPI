package pl.wildfire.api.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.wildfire.api.Msg;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class GuiItem {

    protected final String name;
    protected final List<String> lores;
    protected final ItemStack icon;

    public GuiItem(String name, List<String> lores, ItemStack icon) {
        this.name = Msg.c(name);
        this.lores = Msg.c(lores);
        this.icon = icon;
    }

    public GuiItem(String name, ItemStack icon, String... lores) {
        this(name, Arrays.asList(lores), icon);
    }

    public void onItemClick(ItemClickEvent event) {
    }

    public ItemStack setItemMeta(ItemStack icon) {
        ItemMeta meta = icon.getItemMeta();
        if(name != null && !name.isEmpty())
            meta.setDisplayName(name);
        if(lores != null && !lores.isEmpty())
            meta.setLore(lores);
        icon.setItemMeta(meta);
        return icon;
    }

    public ItemStack getFinalIcon(Player viever) {
        return setItemMeta(getIcon().clone());
    }

}
