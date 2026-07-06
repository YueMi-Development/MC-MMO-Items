package org.yuemi.mmoitems.plugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.event.EventApi;
import org.yuemi.libs.api.event.EventApiProvider;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmoitems.plugin.item.ItemManager;
import org.yuemi.mmoitems.plugin.item.ItemSkillConfig;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class ItemLifecycleListener {

    private final JavaPlugin plugin;
    private final ItemManager itemManager;
    private final Set<UUID> activeCasters = new HashSet<>();

    public ItemLifecycleListener(@NotNull JavaPlugin plugin, @NotNull ItemManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
    }

    public void register() {
        EventApi eventApi = EventApiProvider.getApi();
        if (eventApi == null) {
            plugin.getLogger().warning("YueMiLibs EventApi service not found. Custom item listeners will not be registered!");
            return;
        }

        plugin.getLogger().info("Registering custom item event listeners using YueMiLibs EventApi...");

        // Player attacks with custom item (onAttack)
        eventApi.bukkit().subscribe(EntityDamageByEntityEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    Entity attacker = event.getDamager();
                    if (attacker instanceof Projectile proj && proj.getShooter() instanceof Entity shooter) {
                        attacker = shooter;
                    }

                    if (attacker instanceof Player player) {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        triggerSkills(player, item, "onAttack");
                    }
                });

        // Player right-clicks with custom item (onInteract)
        eventApi.bukkit().subscribe(PlayerInteractEvent.class)
                .priority(EventPriority.MONITOR)
                .handler(event -> {
                    Action action = event.getAction();
                    if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                        Player player = event.getPlayer();
                        ItemStack item = event.getItem();
                        triggerSkills(player, item, "onInteract");
                    }
                });
    }

    private void triggerSkills(@NotNull Player player, @Nullable ItemStack item, @NotNull String triggerType) {
        if (item == null || !itemManager.isCustomItem(item)) {
            return;
        }

        UUID uuid = player.getUniqueId();
        if (activeCasters.contains(uuid)) {
            return; // Prevent recursion loop from damage dealt by skills
        }

        activeCasters.add(uuid);
        try {
            MmoMechanicsApi mechanicsApi = Bukkit.getServicesManager().load(MmoMechanicsApi.class);
            if (mechanicsApi == null) {
                return;
            }
            for (ItemSkillConfig skillConfig : itemManager.getItemSkills(item)) {
                if (skillConfig.trigger() != null && skillConfig.trigger().equalsIgnoreCase(triggerType)) {
                    mechanicsApi.castSkill(uuid, skillConfig.skill());
                }
            }
        } finally {
            activeCasters.remove(uuid);
        }
    }
}
