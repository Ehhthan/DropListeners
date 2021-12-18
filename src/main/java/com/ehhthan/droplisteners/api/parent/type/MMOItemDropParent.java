package com.ehhthan.droplisteners.api.parent.type;

import com.ehhthan.droplisteners.api.parent.DropParent;
import com.google.common.base.Preconditions;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.utils.config.LineConfig;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.VolatileMMOItem;
import net.Indyuce.mmoitems.stat.data.GemstoneData;
import org.apache.commons.lang3.Validate;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class MMOItemDropParent extends DropParent {
    private final Type type;
    private final String id;

    public MMOItemDropParent(LineConfig config) {
        this.type = Type.get(config.getString(new String[]{"type", "t"}));
        this.id = config.getString("id").toUpperCase(Locale.ROOT);

        Preconditions.checkNotNull(type, "Type does not exist.");
        Preconditions.checkNotNull(id, "Id is null.");

        Preconditions.checkNotNull(MMOItems.plugin.getMMOItem(type, id), "MMOItem does not exist: [%s,%s]", type, id);
    }

    @Override
    public boolean isItem(ItemStack item) {
        NBTItem nbtItem = NBTItem.get(item);

        String nbtId = MMOItems.getID(nbtItem);
        Type nbtType = MMOItems.getType(item);

        if (nbtId != null && nbtType != null)
            if (isGemstone()) {
                for (GemstoneData stone : new VolatileMMOItem(nbtItem).getGemStones()) {
                    if (type.getId().equals(stone.getMMOItemType()) && id.equals(stone.getMMOItemID())) {
                        return true;
                    }
                }
            } else {
                return (type == nbtType && id.equals(nbtId));
            }

        return false;
    }

    public boolean isGemstone() {
        return type == Type.GEM_STONE;
    }
}
