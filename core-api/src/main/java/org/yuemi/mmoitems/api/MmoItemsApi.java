package org.yuemi.mmoitems.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public interface MmoItemsApi {

    void sendMessage(
            @NotNull Player player,
            @NotNull String message
    );

    boolean isFeatureEnabled(@NotNull Player player);

    boolean isCustomItem(@NotNull ItemStack item);

    @NotNull Optional<String> getCustomItemId(@NotNull ItemStack item);

    @NotNull ItemStack createCustomItem(@NotNull String itemId);
}
