# Item Model Fix — Full Port for Minecraft 1.20.1 (Forge)

![Mod loader: Forge](https://img.shields.io/badge/modloader-Forge-f14d67?style=for-the-badge)
![Minecraft: 1.20.1](https://img.shields.io/badge/Minecraft-1.20.1-green?style=for-the-badge)
![GitHub license](https://img.shields.io/github/license/Sekai0NI0itamio/ItemModelFix263?style=for-the-badge)

**Fixes gaps in generated item models.**

Vanilla's generated item models shrink UVs, which can leave visible
gaps between pixels on 2D item sprites. Item Model Fix corrects the
generated UVs so item models render seamlessly.

This branch is a **full port** of the original mod to Minecraft 1.20.1
(Forge): the old baked-model `ItemModelGenerator`
(`net.minecraft.client.renderer.block.model.ItemModelGenerator`) still
exists on this version, so the complete original generation logic —
**VANILLA / UNLERP / OUTLINE / PIXEL** — works here, hooked via a
Mixin on `ItemModelGenerator.processFrames`, exactly as in the
original. The 26.3 (main) branch cannot do this because that
generator was removed in 26.3's new cuboid model system.

### Generation modes

- **VANILLA** — original behaviour, no fix applied
- **UNLERP** — vanilla generation with UV math applied to fix gaps
- **OUTLINE** (default) — same as Unlerp, via a different algorithm
  that prevents enchantment-glint flickering
- **PIXEL** — generates every pixel individually; best results, higher
  cost with high-resolution resource packs

### Configuration

No Cloth Config / ModMenu dependency on this port. Edit
`config/itemmodelfix.json` and set `"generationType"` to one of
`VANILLA`, `UNLERP`, `OUTLINE`, `PIXEL`.

### Port notes (1.20.1)

- Java 17 · Forge 47.4.20+ · Mojang official mappings
- Built and verified in CI via GitHub Actions (build + runtime test)

### Installation

1. Install Forge for Minecraft 1.20.1
2. Drop `itemmodelfix-1.20.1-forge-*.jar` into your `mods` folder

### Original mod & credit

This is an unofficial port by **Itamio** of **Item Model Fix** by
**PepperCode1** — the original (926k downloads on Modrinth, last
updated for 1.19.2) has not been updated past 1.19.2.

- Original mod: https://modrinth.com/mod/item-model-fix
- Original source: https://github.com/PepperCode1/Item-Model-Fix
- License: LGPL-3.0-only — see `LICENSE`

All credit for the original concept, design, and logic goes to
PepperCode1. This port exists to bring it to Minecraft 1.20.1. See `NOTICE`.
