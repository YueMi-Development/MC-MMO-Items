package org.yuemi.mmoitems.plugin.item;

import java.util.List;

public record ItemConfigDto(
    String name,
    String material,
    Integer customModelData,
    List<String> lore,
    List<ItemSkillConfig> skills
) {}
