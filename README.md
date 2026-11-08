# Item Model Fix — 26.3 (Fabric) port

Unofficial port of **Item Model Fix** by **PepperCode1**
(https://github.com/PepperCode1/Item-Model-Fix, LGPL-3.0-only, 926k downloads,
max 1.19.2 Fabric) to Minecraft **26.3** (Fabric).

Fixes gaps in generated item models (UV shrinking in generated item
models can leave visible gaps between pixels).

## Credit
Original mod © PepperCode1, LGPL-3.0-only. This port by Itamio, for
Minecraft 26.3. See NOTICE for full credit and licensing.

## Port status
The original mod mixed into the old baked-model
`ItemModelGenerator.addLayerElements(...)` which returned
`List<ModelElement>`. That class/method was removed in 26.3's new
cuboid model system
(`net.minecraft.client.resources.model.cuboid.ItemModelGenerator`,
baking `QuadCollection`s via `bakeExtrudedSprite`/`getSideFaces`).
The outline/pixel/unlerp generation modes therefore need a rewrite
against the new baking pipeline and are **not yet implemented**.

This port includes:
- the original config (`generationType`: VANILLA / UNLERP / OUTLINE / PIXEL),
- the original UV/pixel utilities (`MathUtil`, `PixelDirection`),
- a loadable Fabric mod initializer for 26.3, verified by GitHub Actions.

Reimplementing the generation modes on QuadCollection baking is
tracked as future work.
