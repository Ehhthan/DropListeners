package com.ehhthan.droplisteners.api.parent.type;

import com.ehhthan.droplisteners.api.parent.DropParent;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemDropParent extends DropParent {
    private final Material material;

    public ItemDropParent(LineConfig config) {
        this.material = Material.getMaterial(config.getString(new String[]{"material", "mat", "m"}));
    }

    @Override
    public boolean isItem(ItemStack item) {
        return item.getType() == material;
    }
}
