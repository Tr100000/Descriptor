# Descriptor

A small client-side mod that adds in-game descriptions to Minecraft.

Currently only adds descriptions (in the form of tooltips) for potions and mob effects.

Descriptions for potions show up in item tooltips, and when hovering over effects in the inventory.

The mod has an in-game config available with [Mod Menu](https://modrinth.com/mod/modmenu) and [YetAnotherConfigLib (YACL)](https://modrinth.com/mod/yacl) installed.

## For Modders

To add compatibility with your custom `MobEffect`s, you can simply add an entry to your language file.

```json
{
  "effect.<your-mod-id>.<your-effect-id>.desc": "My custom description"
}
```
