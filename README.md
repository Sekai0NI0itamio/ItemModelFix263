# Item Model Fix — Modern Port for Minecraft 26.3 (Fabric)

![Mod loader: Fabric](https://img.shields.io/badge/modloader-Fabric-1976d2?style=for-the-badge)
![Minecraft: 26.3](https://img.shields.io/badge/Minecraft-26.3-green?style=for-the-badge)
![GitHub license](https://img.shields.io/github/license/Sekai0NI0itamio/ItemModelFix263?style=for-the-badge)

**Fixes gaps in generated item models.**

Vanilla's generated item models shrink UVs, which can leave visible
gaps between pixels on 2D item sprites. Item Model Fix corrects the
generated UVs so item models render seamlessly.

Now with a **modern port for Minecraft 26.3 (Fabric)** — bringing the
original 1.19.2 mod up to today's version, with the original config
and UV/pixel utilities included.

> **Honest port status:** the original mod hooked the old baked-model
> `ItemModelGenerator` (`ModelElement`-based). That generator was
> removed in 26.3's new cuboid model system
> (`net.minecraft.client.resources.model.cuboid.ItemModelGenerator`,
> baking `QuadCollection`s instead of `ModelElement`s), so the
> outline/pixel/unlerp generation modes need a rewrite against the
> new baking pipeline and are **not yet implemented** in this port.
> This port ships the original config (`generationType`: VANILLA /
> UNLERP / OUTLINE / PIXEL) and the UV/pixel utilities (`MathUtil`,
> `PixelDirection`), loads on 26.3, and is verified by GitHub Actions.
> Reimplementing the generation modes on the new pipeline is tracked
> as future work — see NOTICE.

### What the original does

- Fixes UV gaps in generated item models
- Generation modes: VANILLA (original behaviour), UNLERP, OUTLINE, PIXEL

### Modern port notes (26.3)

- Java 25 · Fabric Loader 0.20.1+
- Built and verified in CI via GitHub Actions

### Installation

1. Install Fabric Loader for Minecraft 26.3
2. Drop `itemmodelfix263-*.jar` into your `mods` folder

### Original mod & credit

This is an unofficial modern port by **Itamio** of **Item Model Fix**
by **PepperCode1** — the original (926k downloads on Modrinth, last
updated for 1.19.2) has not been updated past 1.19.2.

- Original mod: https://modrinth.com/mod/item-model-fix
- Original source: https://github.com/PepperCode1/Item-Model-Fix
- CurseForge: https://www.curseforge.com/minecraft/mc-mods/item-model-fix
- License: LGPL-3.0-only — see `LICENSE`

All credit for the original concept, design, and logic goes to
PepperCode1. This port exists to bring it to Minecraft 26.3. See `NOTICE`.
