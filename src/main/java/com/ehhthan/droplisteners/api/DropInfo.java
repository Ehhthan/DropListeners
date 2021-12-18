package com.ehhthan.droplisteners.api;

import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class DropInfo {
    private final Player killer;
    private final Location location;

    private SkillCaster dropper = null;
    private Vector velocity = new Vector();
    private int pickupDelay = 0;

    public DropInfo(Player killer, Location location) {
        this.killer = killer;
        this.location = location;
    }

    public Player getKiller() {
        return killer;
    }

    public Location getLocation() {
        return location;
    }

    public SkillCaster getDropper() {
        return dropper;
    }

    public void setDropper(SkillCaster dropper) {
        this.dropper = dropper;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public int getPickupDelay() {
        return pickupDelay;
    }

    public void setPickupDelay(int pickupDelay) {
        this.pickupDelay = pickupDelay;
    }

    /**
     * Drop an item with the info in this class.
     * @param itemStack Item to drop.
     */
    public void drop(ItemStack itemStack) {
        killer.getWorld().dropItem(location, itemStack, item -> {
            item.setVelocity(velocity);
            item.setPickupDelay(pickupDelay);
        });
    }
}
