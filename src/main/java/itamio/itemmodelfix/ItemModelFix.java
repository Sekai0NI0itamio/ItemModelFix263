package itamio.itemmodelfix;

import itamio.itemmodelfix.config.Config;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unofficial full port of Item Model Fix by PepperCode1 (LGPL-3.0-only)
 * to Minecraft 1.20.1 (Forge). See NOTICE for credit and licensing.
 * On this version the baked-model ItemModelGenerator
 * (net.minecraft.client.renderer.block.model.ItemModelGenerator) still
 * exists, so the full original generation logic
 * (VANILLA / UNLERP / OUTLINE / PIXEL) works here.
 */
@Mod("itemmodelfix")
public class ItemModelFix {
    public static final String ID = "itemmodelfix";
    public static final Logger LOGGER = LoggerFactory.getLogger("ItemModelFix");

    public ItemModelFix() {
        Config.load();
        LOGGER.info("[ItemModelFix] loaded (generationType: {})", Config.get().generationType);
    }
}
