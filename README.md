# Descriptor

A small client-side mod that adds in-game descriptions to Minecraft.

Currently only adds descriptions (in the form of tooltips) for potions and mob effects.

## For Modders

To add compatibility with your custom `MobEffect`s, you can simply add an entry to your language file.

```json
{
  "effect.<your-mod-id>.<your-effect-id>.desc": "My custom description"
}
```
