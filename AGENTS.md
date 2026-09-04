# MC-MMO-Items

Custom item system for PaperMC servers. Creates and manages custom items with skill triggers and type tagging.

## Stack
- PaperMC 1.21+, Java 21, Gradle (Kotlin DSL)
- Soft-depends: YueMiLibs, MmoMechanics

## Item Configuration (JSON5)
```json5
{
  "name": "<gold>Sword of Power",
  "material": "DIAMOND_SWORD",
  "customModelData": 1000,
  "lore": ["<gray>A legendary weapon"],
  "types": ["weapon", "sword", "legendary"],
  "skills": [
    { "skill": "fireball", "trigger": "onAttack" }
  ]
}
```

## Item Types
Customizable tags stored in item PDC. Used by other plugins (e.g., Accessories) to validate item compatibility.

| Method | Purpose |
|--------|---------|
| `hasType(item, type)` | Check if item has specific type tag |
| `getItemTypes(item)` | Get all type tags |
| `getItem(id)` | Get item by ID |
| `createCustomItem(id)` | Recreate item from saved ID |

## Storage
Item PDC: `mmoitems:id` for item ID, `mmoitems:types` for comma-separated types

## Conventions
- Packages: `org.yuemi.mmoitems.{api,plugin}`
- Item configs in `items/*.json5`
- ItemManager handles config loading and item creation
