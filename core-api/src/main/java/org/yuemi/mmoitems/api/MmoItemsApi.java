package org.yuemi.mmoitems.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

/**
 * The public API for managing and querying custom items in MMO-Items.
 */
public interface MmoItemsApi {

    /**
     * Sends a MiniMessage styled message to the player.
     *
     * @param player  the recipient player
     * @param message the MiniMessage formatted message string
     */
    void sendMessage(
            @NotNull Player player,
            @NotNull String message
    );

    /**
     * Checks if the MMO-Items admin features are enabled for the player.
     *
     * @param player the player to check permissions for
     * @return true if features are enabled, false otherwise
     */
    boolean isFeatureEnabled(@NotNull Player player);

    /**
     * Checks if the given ItemStack is a custom MMO-Item.
     *
     * @param item the item stack to check
     * @return true if it contains the custom item PDC tag, false otherwise
     */
    boolean isCustomItem(@NotNull ItemStack item);

    /**
     * Retrieves the custom MMO-Item ID associated with the given ItemStack.
     *
     * @param item the item stack to query
     * @return an Optional containing the custom item ID if present, otherwise empty
     */
    @NotNull Optional<String> getCustomItemId(@NotNull ItemStack item);

    /**
     * Generates a new ItemStack of the custom MMO-Item type.
     *
     * @param itemId the unique registration ID of the custom item configuration
     * @return a new styled ItemStack tagged with the custom item ID
     * @throws IllegalArgumentException if the custom item type is unknown or material is invalid
     */
    @NotNull ItemStack createCustomItem(@NotNull String itemId);
}
