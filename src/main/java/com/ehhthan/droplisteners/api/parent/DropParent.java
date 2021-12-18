package com.ehhthan.droplisteners.api.parent;

import com.ehhthan.droplisteners.api.parent.type.ItemDropParent;
import com.ehhthan.droplisteners.api.parent.type.MMOItemDropParent;
import io.lumine.mythic.utils.config.LineConfig;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public abstract class DropParent {
    public static DropParent of(LineConfig config) {
        String key = config.getKey().toLowerCase(Locale.ROOT);
        return switch (key) {
            case "mmoitem" -> new MMOItemDropParent(config);
            case "item" -> new ItemDropParent(config);
            default -> throw new IllegalArgumentException(key + " is not a valid DropParent.");
        };
    }

    public abstract boolean isItem(ItemStack item);
}
