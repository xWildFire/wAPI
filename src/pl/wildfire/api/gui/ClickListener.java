package pl.wildfire.api.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ClickListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof GuiHolder) {
            ((GuiHolder) event.getInventory().getHolder()).getMenu().onInventoryClick(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof GuiHolder) {
            ((GuiHolder) event.getInventory().getHolder()).getMenu().onClose(event);
        }
    }

}
