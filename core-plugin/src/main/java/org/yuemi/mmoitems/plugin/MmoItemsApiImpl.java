package org.yuemi.mmoitems.plugin;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmoitems.api.MmoItemsApi;

final class MmoItemsApiImpl implements MmoItemsApi {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

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
}
