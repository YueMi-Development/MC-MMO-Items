package org.yuemi.mmoitems.plugin.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.items.ItemProvider;

import java.util.Map;

public final class MmoItemProvider implements ItemProvider {

    private final ItemManager itemManager;

    public MmoItemProvider(@NotNull ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public @NotNull String getName() {
        return "MMOItems";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public @Nullable ItemStack getItem(@NotNull String id, int amount) {
        try {
            ItemStack item = itemManager.createCustomItem(id);
            item.setAmount(amount);
            return item;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public boolean giveItem(@NotNull Player player, @NotNull String id, int amount) {
        ItemStack item = getItem(id, amount);
        if (item == null) {
            return false;
        }
        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
        if (!leftovers.isEmpty()) {
            for (ItemStack leftover : leftovers.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), leftover);
            }
        }
        return true;
    }

    @Override
    public boolean takeItem(@NotNull Player player, @NotNull String id, int amount) {
        if (amount <= 0) {
            return true;
        }
        if (getItemCount(player, id) < amount) {
            return false;
        }

        int remaining = amount;
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && matches(item, id)) {
                int count = item.getAmount();
                if (count <= remaining) {
                    remaining -= count;
                    player.getInventory().setItem(i, null);
                } else {
                    item.setAmount(count - remaining);
                    remaining = 0;
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public int getItemCount(@NotNull Player player, @NotNull String id) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && matches(item, id)) {
                count += item.getAmount();
            }
        }
        return count;
    }

    private boolean matches(@NotNull ItemStack item, @NotNull String id) {
        return itemManager.isCustomItem(item) &&
               itemManager.getCustomItemId(item)
                          .map(itemId -> itemId.equalsIgnoreCase(id))
                          .orElse(false);
    }
}
