package org.yuemi.mmoitems.plugin.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.URI;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.stream.Collectors;

public final class ItemManager {

    private final JavaPlugin plugin;
    private final Map<String, ItemConfigDto> loadedConfigs = new HashMap<>();
    private final ObjectMapper mapper;
    private final NamespacedKey idKey;

    public ItemManager(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.idKey = new NamespacedKey(plugin, "id");
        this.mapper = JsonMapper.builder()
                .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
                .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
                .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(JsonReadFeature.ALLOW_TRAILING_COMMA)
                .build();
    }

    public void loadConfigs() {
        loadedConfigs.clear();

        // 1. Auto-extract default configs from Jar resources
        try {
            URI jarUri = plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            File jarFile = new File(jarUri);
            if (jarFile.exists() && jarFile.isFile()) {
                try (ZipFile zip = new ZipFile(jarFile)) {
                    Enumeration<? extends ZipEntry> entries = zip.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.startsWith("items/") && name.endsWith(".json5")) {
                            File outFile = new File(plugin.getDataFolder(), name);
                            if (!outFile.exists()) {
                                try {
                                    plugin.saveResource(name, false);
                                } catch (IllegalArgumentException ignored) {
                                    // Already exists
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to extract default items: " + e.getMessage());
        }

        // 2. Load configurations from items/ directory
        File itemsFolder = new File(plugin.getDataFolder(), "items");
        if (!itemsFolder.exists()) {
            itemsFolder.mkdirs();
        }

        File[] files = itemsFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".json5")) {
                    try {
                        ItemConfigDto dto = mapper.readValue(file, ItemConfigDto.class);
                        String itemTypeId = file.getName().substring(0, file.getName().lastIndexOf('.')).toLowerCase();
                        loadedConfigs.put(itemTypeId, dto);
                        plugin.getLogger().info("Loaded item configuration: " + dto.name() + " (" + file.getName() + ")");
                    } catch (Exception e) {
                        plugin.getLogger().severe("Failed to load item file " + file.getName() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @NotNull
    public ItemStack createCustomItem(@NotNull String itemId) {
        String cleanId = itemId.toLowerCase();
        ItemConfigDto config = loadedConfigs.get(cleanId);
        if (config == null) {
            throw new IllegalArgumentException("Unknown custom item type: " + itemId);
        }

        ItemStack item;
        if (config.material().contains(":")) {
            try {
                org.yuemi.libs.api.items.ItemsApi itemsApi = org.yuemi.libs.api.items.ItemsApiProvider.getApi();
                if (itemsApi != null) {
                    item = itemsApi.getItem(config.material(), 1);
                    if (item == null) {
                        throw new IllegalArgumentException("Provider returned null for item '" + config.material() + "'");
                    }
                } else {
                    throw new IllegalStateException("YueMi Libs ItemsApi is not available");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to load custom material '" + config.material() + "' for item " + itemId + ": " + e.getMessage(), e);
            }
        } else {
            Material material;
            try {
                material = Material.valueOf(config.material().toUpperCase());
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid material '" + config.material() + "' for item: " + itemId);
            }
            item = new ItemStack(material);
        }
        var meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        boolean isCustomProviderItem = config.material().contains(":") && !config.material().startsWith("minecraft:");
        String policy = plugin.getConfig().getString("custom-items-policy", "FALLBACK").toUpperCase();
        boolean forceOverride = "OVERRIDE".equals(policy);

        if (config.name() != null) {
            if (!isCustomProviderItem || forceOverride || !meta.hasDisplayName()) {
                meta.displayName(MiniMessage.miniMessage().deserialize(config.name()));
            }
        }

        if (config.lore() != null) {
            if (!isCustomProviderItem || forceOverride || !meta.hasLore()) {
                List<net.kyori.adventure.text.Component> loreComponents = config.lore().stream()
                        .map(line -> MiniMessage.miniMessage().deserialize(line))
                        .collect(Collectors.toList());
                meta.lore(loreComponents);
            }
        }

        if (config.customModelData() != null) {
            if (!isCustomProviderItem || forceOverride || !meta.hasCustomModelData()) {
                meta.setCustomModelData(config.customModelData());
            }
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(idKey, PersistentDataType.STRING, cleanId);

        item.setItemMeta(meta);
        return item;
    }

    public boolean isCustomItem(@Nullable ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().has(idKey, PersistentDataType.STRING);
    }

    @NotNull
    public Optional<String> getCustomItemId(@Nullable ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return Optional.empty();
        }
        return Optional.ofNullable(item.getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING));
    }

    @NotNull
    public List<ItemSkillConfig> getItemSkills(@Nullable ItemStack item) {
        if (!isCustomItem(item)) {
            return Collections.emptyList();
        }
        String itemId = getCustomItemId(item).orElse("");
        ItemConfigDto config = loadedConfigs.get(itemId);
        if (config != null && config.skills() != null) {
            return config.skills();
        }
        return Collections.emptyList();
    }

    @NotNull
    public Collection<String> getRegisteredItemTypes() {
        return loadedConfigs.keySet();
    }
}
