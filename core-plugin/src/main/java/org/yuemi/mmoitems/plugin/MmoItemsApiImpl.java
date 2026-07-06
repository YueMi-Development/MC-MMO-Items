package org.yuemi.mmoitems.plugin;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmoitems.api.MmoItemsApi;
import org.yuemi.mmoitems.plugin.item.ItemManager;

import java.util.Optional;

final class MmoItemsApiImpl implements MmoItemsApi {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final ItemManager itemManager;

    public MmoItemsApiImpl(@NotNull ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public void sendMessage(
            @NotNull Player player,
            @NotNull String message
    ) {
        player.sendMessage(miniMessage.deserialize(message));
    }

    @Override
    public boolean isFeatureEnabled(@NotNull Player player) {
        return player.hasPermission("mmoitems.feature");
    }

    @Override
    public boolean isCustomItem(@NotNull ItemStack item) {
        return itemManager.isCustomItem(item);
    }

    @Override
    public @NotNull Optional<String> getCustomItemId(@NotNull ItemStack item) {
        return itemManager.getCustomItemId(item);
    }

    @Override
    public @NotNull ItemStack createCustomItem(@NotNull String itemId) {
        return itemManager.createCustomItem(itemId);
    }
}

