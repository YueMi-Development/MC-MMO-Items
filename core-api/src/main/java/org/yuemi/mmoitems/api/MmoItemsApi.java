package org.yuemi.mmoitems.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MmoItemsApi {

    void sendMessage(
            @NotNull Player player,
            @NotNull String message
    );

    boolean isFeatureEnabled(@NotNull Player player);
}
