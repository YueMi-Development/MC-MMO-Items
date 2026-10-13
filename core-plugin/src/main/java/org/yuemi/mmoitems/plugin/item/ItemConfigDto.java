package org.yuemi.mmoitems.plugin.item;

import org.yuemi.mmoitems.api.item.ItemSkillConfig;
import java.util.List;

public record ItemConfigDto(
    String name,
    String material,
    Integer customModelData,
    List<String> lore,
    List<ItemSkillConfig> skills,
    List<String> types
) {}
