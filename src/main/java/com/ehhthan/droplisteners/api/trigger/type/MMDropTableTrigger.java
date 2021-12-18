package com.ehhthan.droplisteners.api.trigger.type;

import com.ehhthan.droplisteners.api.DropInfo;
import com.ehhthan.droplisteners.api.trigger.DropTrigger;
import io.lumine.mythic.utils.config.LineConfig;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.DropTable;
import io.lumine.xikage.mythicmobs.drops.IItemDrop;
import io.lumine.xikage.mythicmobs.drops.ILocationDrop;
import org.apache.commons.lang3.Validate;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Optional;

public class MMDropTableTrigger extends DropTrigger {
    private final DropTable dropTable;

    public MMDropTableTrigger(LineConfig config) {
        Optional<DropTable> dropTable = MythicMobs.inst().getDropManager().getDropTable(config.getString("id"));
        Validate.isTrue(dropTable.isPresent(), "Drop table does not exist.");

        this.dropTable = dropTable.get();
    }

    @Override
    public boolean drop(DropInfo info) {
        DropMetadata metadata = new DropMetadata(info.getDropper(), BukkitAdapter.adapt(info.getKiller()));
        Collection<Drop> drops = dropTable.generate(metadata).getDrops();

        for (Drop drop : drops) {
            if (drop instanceof IItemDrop) {
                // Adapt item to bukkit format and drop it with the specified info.
                ItemStack itemDrop = BukkitAdapter.adapt(((IItemDrop) drop).getDrop(metadata));
                info.drop(itemDrop);

            } else if (drop instanceof ILocationDrop) {
                ((ILocationDrop)drop).drop(BukkitAdapter.adapt(info.getLocation()), metadata);
            }
        }

        // Returns false if no items were dropped from this table.
        return drops.size() > 0;
    }
}
