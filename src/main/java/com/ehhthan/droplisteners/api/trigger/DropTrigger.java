package com.ehhthan.droplisteners.api.trigger;

import com.ehhthan.droplisteners.api.DropInfo;
import com.ehhthan.droplisteners.api.trigger.type.MMDropTableTrigger;
import io.lumine.mythic.utils.config.LineConfig;

import java.util.Locale;

public abstract class DropTrigger {
    public static DropTrigger of(LineConfig config) {
        String key = config.getKey().toLowerCase(Locale.ROOT);
        return switch (key) {
            case "mmdroptable" -> new MMDropTableTrigger(config);
            default -> throw new IllegalArgumentException(key + " is not a valid DropTrigger.");
        };
    }

    public abstract boolean drop(DropInfo info);
}
