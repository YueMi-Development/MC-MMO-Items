package org.yuemi.mmoitems.api.item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Describes a single skill bound to a custom MmoItems item.
 *
 * <p>Loaded from the item's JSON5 config under the {@code skills[]} key.
 * Each skill can either be a string (skill id only; default trigger)
 * or an object with explicit {@code skill} and {@code trigger} fields.
 *
 * @param skill   the skill id, as registered in MmoMechanics
 * @param trigger the trigger id that fires this skill, or null if unspecified
 */
public record ItemSkillConfig(@NotNull String skill, @Nullable String trigger) {
}
