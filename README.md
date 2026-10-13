# MC-MMO-Items

Define custom items (weapons, armor, accessories, consumables) in JSON5 and bind MMO Mechanics skills to them. MC-MMO-Items is the data layer that MC-MMO-Accessories, MMO-Mechanics trigger execution, and your own plugins all read from.

<a href="https://modrinth.com/plugin/eIR7cMWa" target="_blank" rel="noopener noreferrer"><img alt="modrinth" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg"></a>

![](https://img.shields.io/bstats/players/34672?color=green&label=Players)
![](https://img.shields.io/bstats/servers/34672?color=green&label=Servers)

---

## Features

### 1. JSON5 Item Configuration
- **Comments, single quotes, unquoted field names, trailing commas** — write item definitions in human-friendly JSON5.
- **Persistent Identification** — every custom item is tagged with `mmoitems:id` and a comma-joined `mmoitems:types` list on its `PersistentDataContainer` so it survives restarts and inventory moves.
- **Multi-Type Items** — assign any number of type tags per item. MC-MMO-Accessories uses these for any-of matching against slot `accepted-types`.

### 2. Material Provider Integration
- **YueMiLibs `mmoitems:` namespace** — registered automatically. Other plugins and menus can resolve, give, take, or count items using the `mmoitems:itemId` syntax.
- **Multiple material sources**: vanilla `minecraft:`, `craftengine:`, `itemsadder:`, `nexo:` — pulled through YueMiLibs' `ItemsApi` so the same item config works regardless of which provider supplies the underlying item.

### 3. Skill Bindings
- **`skills: [{ skill, trigger }]`** in each item config declares which MMO Mechanics skills fire on which event.
- **Public Lookup**: `MmoItemsApi.getItemSkills(ItemStack)` returns the bound skills as `List<ItemSkillConfig>` (the public record in `org.yuemi.mmoitems.api.item`). MMO Mechanics and MC-MMO-Accessories consume this surface directly.
- **Standard Triggers**: `onAttack`, `onInteract`, `onEquip`, `onUnequip`, `onDamaged` — interpreted by MMO Mechanics. See [MMO Mechanics skill config](https://github.com/YueMi-Development/MC-MMO-Mechanics) for the full list.

### 4. Default Example Items
The plugin ships with seven ready-to-use accessories: Steel Belt, Enchanted Cloth Gloves, Reinforced Leather Gloves, Ruby Ring, Sapphire Ring, Amulet of Vitality, Pendant of Speed. Drop the JSON5 files in `items/` and they're loaded on `/mmoitems reload`.

---

## Commands

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/mmoitems give <player> <itemId> [amount]` | Give a custom item | `mmoitems.admin` |
| `/mmoitems list` | List all registered items | `mmoitems.admin` |
| `/mmoitems reload` | Reload item configs | `mmoitems.admin` |

Aliases: `/items`.
