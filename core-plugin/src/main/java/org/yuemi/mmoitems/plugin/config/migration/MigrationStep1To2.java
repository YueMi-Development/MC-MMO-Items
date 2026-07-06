package org.yuemi.mmoitems.plugin.config.migration;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * Migration step to target version 2. Adds the custom-items-policy option.
 */
public final class MigrationStep1To2 implements MigrationStep {

    @Override
    public int getTargetVersion() {
        return 2;
    }

    @Override
    public void migrate(@NotNull YamlConfiguration configuration) {
        if (!configuration.contains("custom-items-policy")) {
            configuration.set("custom-items-policy", "FALLBACK");
        }
    }
}
