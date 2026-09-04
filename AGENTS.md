# MC-MMO-Items

Custom item system for PaperMC servers. Creates and manages custom items with PDC tagging.

## Stack
- PaperMC 1.21+, Java 21, Gradle (Kotlin DSL), JSON5, MiniMessage
- Dependencies: Paper API, YueMiLibs

## API
- `MmoItemsApiProvider.getApi()` - Main API
- `isCustomItem()`, `getCustomItemId()`, `createCustomItem()`

## Conventions
- Packages: `org.yuemi.mmoitems.{api,plugin,plugin.command,plugin.item}`
- Classes: PascalCase, `*Dto`, `*Command`, `*Listener`, `*Manager`, `*Provider`
- Methods/vars: camelCase, CONSTANTS: UPPER_SNAKE_CASE

## Best Practices
- Validate material types
- Use Optional for nullable returns
- Log errors with filename
- MiniMessage for formatted text

## Build
```
./gradlew build
```
