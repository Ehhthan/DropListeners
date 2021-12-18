package com.ehhthan.droplisteners.api.listeners.type;

import com.ehhthan.droplisteners.DropListeners;
import com.ehhthan.droplisteners.api.DropInfo;
import com.ehhthan.droplisteners.api.listeners.DropListener;
import com.ehhthan.droplisteners.api.parent.DropParent;
import com.ehhthan.droplisteners.api.trigger.DropTrigger;
import com.google.common.base.Preconditions;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FishingListener implements DropListener<PlayerFishEvent> {
    private final Map<DropParent, List<DropTrigger>> drops = new LinkedHashMap<>();

    public FishingListener(ConfigurationSection section) {
        reload(section);
    }

    public void reload(ConfigurationSection section) {
        drops.clear();

        for (String key : section.getKeys(false)) {
            if (section.isConfigurationSection(key)) {
                ConfigurationSection drop = section.getConfigurationSection(key);

                // Check required fields exist.
                Preconditions.checkArgument(drop.isString("parent"), "%s Error: Parent does not exist.", drop.getName());
                Preconditions.checkArgument(drop.isList("drops"), "%s Error: Drops do not exist.", drop.getName());

                // Get parent by converting String > LineConfig > DropParent.
                DropParent parent = DropParent.of(LineConfig.of(drop.getString("parent", "")));

                List<DropTrigger> triggers = new LinkedList<>();
                for (String trigger : drop.getStringList("drops")) {
                    // Get parent by converting String > LineConfig > DropTrigger.
                    triggers.add(DropTrigger.of(LineConfig.of(trigger)));
                }

                // Store DropTrigger collection with the key of the DropParent.
                drops.put(parent, triggers);
            }
        }
    }

    @Override
    @EventHandler
    public void onDropEvent(PlayerFishEvent event) {
        //Check if event has caught something and that catch is an item.
        if (event.getCaught() != null && event.getCaught() instanceof Item caught) {
            // Rods can be cast from either hand so use some logic to determine which hand held the rod.
            PlayerInventory inv = event.getPlayer().getInventory();
            ItemStack rod = (inv.getItemInMainHand().getType() != Material.AIR)
                ? inv.getItemInMainHand()
                : inv.getItemInOffHand();

            // Stores the original item and then removes the entity before it even appears.
            ItemStack caughtItem = caught.getItemStack();
            caught.remove();

            // This needs to be delayed or else item's velocity will be incorrect.
            Bukkit.getScheduler().scheduleSyncDelayedTask(DropListeners.getInstance(), () -> {
                DropInfo info = new DropInfo(event.getPlayer(), event.getCaught().getLocation());
                info.setPickupDelay(caught.getPickupDelay());
                info.setVelocity(caught.getVelocity());

                for (Map.Entry<DropParent, List<DropTrigger>> entry : drops.entrySet()) {
                    if (entry.getKey().isItem(rod)) {
                        for (DropTrigger trigger : entry.getValue()) {
                            if (trigger.drop(info)) {
                                return;
                            }
                        }
                    }
                }

                // If none of the other items drop, the original item is dropped.
                info.drop(caughtItem);
            }, 1L);

        }
    }
}
